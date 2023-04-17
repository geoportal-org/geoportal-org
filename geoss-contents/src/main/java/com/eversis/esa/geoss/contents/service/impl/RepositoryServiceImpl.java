package com.eversis.esa.geoss.contents.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.eversis.esa.geoss.contents.configuration.RepositoryProperties;
import com.eversis.esa.geoss.contents.domain.Document;
import com.eversis.esa.geoss.contents.domain.Folder;
import com.eversis.esa.geoss.contents.exception.FileNameNotUniqueException;
import com.eversis.esa.geoss.contents.repository.DocumentRepository;
import com.eversis.esa.geoss.contents.repository.FolderRepository;
import com.eversis.esa.geoss.contents.service.RepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Repository service.
 */
@Slf4j
@Service
public class RepositoryServiceImpl implements RepositoryService {

    private static final int rootFolderId = 0;
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

    @Override
    public void init() {
        try {
            log.info("Creating the root directory for uploaded files storage.");
            Path rootFolderDirectory = rootDirectory.resolve(String.valueOf(rootFolderId));
            Files.createDirectories(rootFolderDirectory);
            log.info("Root folder directory created at path {}", rootFolderDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the root directory where the uploaded files will be stored.");
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
        Folder removedfolder = folderRepository.getById(id);
        Path path = Paths.get(removedfolder.getPath());
        Path removeDirectory = rootDirectory.resolve(path).resolve(String.valueOf(id));
        FileSystemUtils.deleteRecursively(removeDirectory.toFile());
        deleteDocumentsAndFoldersRecursively(id);
        log.info("Removed folder at path {}", removeDirectory);
    }

    @Transactional
    public void deleteDocumentsAndFoldersRecursively(Long folderId) {
        Folder folder = folderRepository.findById(folderId).orElse(null);
        if (folder != null) {
            log.debug("folder: {}", folder);
            Pageable paging = PageRequest.of(0, Integer.MAX_VALUE);
            Page<Folder> folderPage = folderRepository.findByParentFolderId(folderId.toString(), paging);
            List<Folder> subFolders = folderPage.getContent();
            log.debug("subFolders: {}", subFolders);
            subFolders.forEach(subfolder -> deleteDocumentsAndFoldersRecursively(subfolder.getId()));
            Page<Document> documentPage = documentRepository.findByFolderId(folderId.toString(), paging);
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
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        log.info("Document added.");
        return savedDocument;
    }

    @Override
    public void removeDocument(Long id) {
        log.info("Removing document with id {}", id);
        Document document = documentRepository.getById(id);
        Path path = Paths.get(document.getPath());
        Path removePath = rootDirectory.resolve(path).resolve(document.getFileName());
        FileSystemUtils.deleteRecursively(removePath.toFile());
        documentRepository.deleteById(id);
        log.info("Removed document at path {}", removePath);
    }

    @Override
    public Resource getDocumentContent(Long id) {
        log.info("Getting document with id {}", id);
        Document document = documentRepository.getById(id);
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
    }

    @Override
    public void validateFileNameUnique(Document document) {
        int page = 0;
        int size = Integer.MAX_VALUE;
        Pageable paging = PageRequest.of(page, size);
        Page<Document> documentPage = documentRepository.findByFolderId(document.getFolderId().toString(), paging);
        List<Document> documents = documentPage.getContent();

        boolean isFileNameNotUnique = documents.stream()
                .anyMatch(doc -> doc.getFileName().equals(document.getFileName()));

        if (isFileNameNotUnique) {
            throw new FileNameNotUniqueException("fileName not unique");
        }
    }

}
