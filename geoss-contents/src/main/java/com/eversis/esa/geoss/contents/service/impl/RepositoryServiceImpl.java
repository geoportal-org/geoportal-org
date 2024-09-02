package com.eversis.esa.geoss.contents.service.impl;

import com.eversis.esa.geoss.contents.configuration.RepositoryProperties;
import com.eversis.esa.geoss.contents.domain.Document;
import com.eversis.esa.geoss.contents.domain.Folder;
import com.eversis.esa.geoss.contents.exception.FileNameNotUniqueException;
import com.eversis.esa.geoss.contents.repository.DocumentRepository;
import com.eversis.esa.geoss.contents.repository.FolderRepository;
import com.eversis.esa.geoss.contents.service.RepositoryService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * The type Repository service.
 */
@Slf4j
@Service
public class RepositoryServiceImpl implements RepositoryService {

    private static final int GLOBAL_SITE_FOLDER_ID = 0;
    private static final int LOGO_FOLDER_ID = 1;
    private final Path rootDirectory;
    private final FolderRepository folderRepository;
    private final DocumentRepository documentRepository;

    /**
     * Instantiates a new Repository service.
     *
     * @param folderRepository the folder repository
     * @param documentRepository the document repository
     * @param repositoryProperties the repository properties
     */
    @Autowired
    public RepositoryServiceImpl(FolderRepository folderRepository, DocumentRepository documentRepository,
            RepositoryProperties repositoryProperties) {
        this.folderRepository = folderRepository;
        this.documentRepository = documentRepository;
        this.rootDirectory = Paths.get(repositoryProperties.getDirectory()).toAbsolutePath().normalize();
    }

    /**
     * Initializes the file storage by creating the root directory and the logo directory, and uploading the global site
     * logo.
     */
    @Override
    public void init() {
        try {
            log.info("Initializing file storage...");
            createDirectory(rootDirectory.resolve(String.valueOf(GLOBAL_SITE_FOLDER_ID)), "global site folder");
            createDirectory(rootDirectory.resolve(String.valueOf(GLOBAL_SITE_FOLDER_ID))
                    .resolve(String.valueOf(LOGO_FOLDER_ID)), "logo folder");
            uploadResource("logo/geoss-portal.png",
                    rootDirectory.resolve(String.valueOf(GLOBAL_SITE_FOLDER_ID))
                            .resolve(String.valueOf(LOGO_FOLDER_ID)));
            log.info("File storage initialization completed.");
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize file storage.", e);
        }
    }

    @Override
    public Folder addFolder(Folder folder) {
        log.info("Adding folder {}", folder);
        Folder savedFolder = folderRepository.save(folder);
        try {
            Path path = Paths.get(savedFolder.getPath());
            Path newDirectory = rootDirectory.resolve(path).resolve(String.valueOf(savedFolder.getId()));
            Files.createDirectories(newDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the folder.");
        }
        log.info("Folder added.");
        return savedFolder;
    }

    @Override
    public void removeFolder(Long id) {
        log.info("Removing folder with id {}", id);
        folderRepository.findById(id)
                .map(removedfolder -> {
                    Path path = Paths.get(removedfolder.getPath());
                    Path removeDirectory = rootDirectory.resolve(path).resolve(String.valueOf(id));
                    FileSystemUtils.deleteRecursively(removeDirectory.toFile());
                    deleteDocumentsAndFoldersRecursively(id);
                    log.info("Removed folder at path {}", removeDirectory);
                    return removedfolder;
                }).orElseThrow(() -> new ResourceNotFoundException("Folder not found"));
    }

    @Override
    @Transactional
    public void deleteDocumentsAndFoldersRecursively(Long folderId) {
        Folder folder = folderRepository.findById(folderId).orElse(null);
        if (folder != null) {
            log.debug("folder: {}", folder);
            Pageable paging = PageRequest.of(0, Integer.MAX_VALUE);
            Page<Folder> folderPage = folderRepository.findByParentFolderId(folderId, paging);
            List<Folder> subFolders = folderPage.getContent();
            log.debug("subFolders: {}", subFolders);
            subFolders.forEach(subfolder -> deleteDocumentsAndFoldersRecursively(subfolder.getId()));
            Page<Document> documentPage = documentRepository.findByFolderId(folderId, paging);
            List<Document> documents = documentPage.getContent();
            log.debug("documents: {}", documents);
            documents.forEach(document -> documentRepository.delete(document));
            folderRepository.delete(folder);
        }
    }

    @Override
    public Document addDocument(Document document, MultipartFile file) {
        log.info("Adding document {}", document);
        Document savedDocument = documentRepository.save(document);
        try {
            Path path = Paths.get(document.getPath());
            Path uploadPath = rootDirectory.resolve(path);
            Files.copy(file.getInputStream(), uploadPath.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage(), e);
        }
        log.info("Document added.");
        return savedDocument;
    }

    @Override
    public void removeDocument(Long id) {
        log.info("Removing document with id {}", id);
        documentRepository.findById(id)
                .map(document -> {
                    Path path = Paths.get(document.getPath());
                    Path removePath = rootDirectory.resolve(path).resolve(document.getFileName());
                    FileSystemUtils.deleteRecursively(removePath.toFile());
                    documentRepository.deleteById(id);
                    log.info("Removed document at path {}", removePath);
                    return document;
                }).orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    @Override
    public Resource getDocumentContent(Long id) {
        log.info("Getting document with id {}", id);
        return documentRepository.findById(id)
                .map(document -> {
                    log.info("document {}", document);
                    try {
                        Path path = Paths.get(document.getPath());
                        Path filePath = rootDirectory.resolve(path).resolve(document.getFileName());
                        log.info("filePath {}", filePath);
                        Resource resource = new UrlResource(filePath.toUri());
                        if (resource.exists() || resource.isReadable()) {
                            log.info("resource {}", resource);
                            return resource;
                        } else {
                            throw new RuntimeException("Could not read the file!");
                        }
                    } catch (MalformedURLException e) {
                        throw new RuntimeException("Error: " + e.getMessage());
                    }
                }).orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    @Override
    public void validateFileNameUnique(Document document) {
        int page = 0;
        int size = Integer.MAX_VALUE;
        Pageable paging = PageRequest.of(page, size);
        Page<Document> documentPage = documentRepository.findByFolderIdAndSiteId(document.getFolderId(),
                document.getSiteId(), paging);
        List<Document> documents = documentPage.getContent();

        boolean isFileNameNotUnique = documents.stream()
                .anyMatch(doc -> doc.getFileName().equals(document.getFileName()));

        if (isFileNameNotUnique) {
            throw new FileNameNotUniqueException("fileName not unique");
        }
    }

    private void uploadResource(String resourcePath, Path targetDirectory) throws IOException {
        log.info("Uploading resource {}...", resourcePath);
        Resource resource = new ClassPathResource(resourcePath);
        Path targetPath = targetDirectory.resolve(resource.getFilename());

        try {
            Files.copy(resource.getInputStream(), targetPath);
            log.info("Resource {} uploaded to {}", resourcePath, targetDirectory);
        } catch (FileAlreadyExistsException e) {
            log.error("File already exists at {}. Skipping upload.", targetPath);
        } catch (IOException e) {
            log.error("Error occurred while uploading resource: {}", e.getMessage());
        }
    }

    private void createDirectory(Path path, String description) throws IOException {
        log.info("Creating the {} directory...", description);
        Files.createDirectories(path);
        log.info("{} directory created at path {}", description, path);
    }

}
