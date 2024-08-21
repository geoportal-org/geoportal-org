package com.eversis.esa.geoss.contents.service.impl;

import com.eversis.esa.geoss.contents.configuration.RepositoryProperties;
import com.eversis.esa.geoss.contents.domain.Document;
import com.eversis.esa.geoss.contents.domain.Folder;
import com.eversis.esa.geoss.contents.domain.Menu;
import com.eversis.esa.geoss.contents.domain.Site;
import com.eversis.esa.geoss.contents.parser.ContentCsvParser;
import com.eversis.esa.geoss.contents.parser.MenuCsvParser;
import com.eversis.esa.geoss.contents.parser.PageCsvParser;
import com.eversis.esa.geoss.contents.repository.ContentRepository;
import com.eversis.esa.geoss.contents.repository.DocumentRepository;
import com.eversis.esa.geoss.contents.repository.FolderRepository;
import com.eversis.esa.geoss.contents.repository.MenuRepository;
import com.eversis.esa.geoss.contents.repository.PageRepository;
import com.eversis.esa.geoss.contents.repository.SiteRepository;
import com.eversis.esa.geoss.contents.service.RepositoryService;
import com.eversis.esa.geoss.contents.service.SiteService;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the SiteService interface. This service manages the creation and deletion of sites.
 */
@Slf4j
@Service
public class SiteServiceImpl implements SiteService {

    /**
     * The constant DEFAULT_LOGO_DOCUMENT_ID.
     */
    public static final long DEFAULT_LOGO_DOCUMENT_ID = 1L;

    private final SiteRepository siteRepository;
    private final ContentRepository contentRepository;
    private final DocumentRepository documentRepository;
    private final FolderRepository folderRepository;
    private final MenuRepository menuRepository;
    private final PageRepository pageRepository;
    private final ContentCsvParser contentCsvParser;
    private final PageCsvParser pageCsvParser;
    private final MenuCsvParser menuCsvParser;
    private final RepositoryService repositoryService;
    private final Path rootDirectory;

    /**
     * Instantiates a new Site service.
     *
     * @param siteRepository the site repository
     * @param contentRepository the content repository
     * @param documentRepository the document repository
     * @param folderRepository the folder repository
     * @param menuRepository the menu repository
     * @param pageRepository the page repository
     * @param contentCsvParser the content csv parser
     * @param pageCsvParser the page csv parser
     * @param menuCsvParser the menu csv parser
     * @param repositoryService the repository service
     * @param repositoryProperties the repository properties
     */
    public SiteServiceImpl(SiteRepository siteRepository, ContentRepository contentRepository,
            DocumentRepository documentRepository, FolderRepository folderRepository, MenuRepository menuRepository,
            PageRepository pageRepository, ContentCsvParser contentCsvParser, PageCsvParser pageCsvParser,
            MenuCsvParser menuCsvParser, RepositoryService repositoryService,
            RepositoryProperties repositoryProperties) {
        this.siteRepository = siteRepository;
        this.contentRepository = contentRepository;
        this.documentRepository = documentRepository;
        this.folderRepository = folderRepository;
        this.menuRepository = menuRepository;
        this.pageRepository = pageRepository;
        this.contentCsvParser = contentCsvParser;
        this.pageCsvParser = pageCsvParser;
        this.menuCsvParser = menuCsvParser;
        this.repositoryService = repositoryService;
        this.rootDirectory = Paths.get(repositoryProperties.getDirectory()).toAbsolutePath().normalize();
    }

    /**
     * Deletes a site and all related entities.
     *
     * @param siteId The ID of the site to be deleted.
     */
    @Override
    @Transactional
    public void deleteSite(long siteId) {
        log.info("Deleting site with id: {}", siteId);
        deleteSiteEntities(siteId);
        log.info("Site with id: {} deleted", siteId);
    }

    @Override
    @Transactional
    public Site createSite(Site site, MultipartFile file) {
        log.info("Creating site: {}", site);
        site.setLogoDocumentId(DEFAULT_LOGO_DOCUMENT_ID);
        Site createdSite = siteRepository.save(site);
        long logoId = initFileStorage(createdSite.getId(), file);
        site.setLogoDocumentId(logoId);
        siteRepository.save(site);
        loadInitData(createdSite.getId());
        log.info("Site created: {}", createdSite);
        return createdSite;
    }

    @Override
    public boolean existsByUrl(String url) {
        return siteRepository.findByUrl(url) != null;
    }

    private void deleteSiteEntities(long siteId) {
        menuRepository.deleteBySiteId(siteId);
        pageRepository.deleteBySiteId(siteId);
        contentRepository.deleteBySiteId(siteId);
        documentRepository.deleteBySiteId(siteId);
        folderRepository.deleteBySiteId(siteId);
        removeFileStorage(siteId);
        siteRepository.deleteById(siteId);
    }

    private void removeFileStorage(long siteId) {
        log.info("Removing file storage for site with id: {}", siteId);
        Path sitePath = Paths.get(String.valueOf(siteId));
        Path removeDirectory = rootDirectory.resolve(sitePath);
        FileSystemUtils.deleteRecursively(removeDirectory.toFile());
        log.info("File storage removed for site with id: {}", siteId);
    }

    @SneakyThrows
    private long initFileStorage(Long siteId, MultipartFile file) {
        log.info("Initializing file storage...");
        Folder logoFolder = createLogoFolder(siteId);
        Document logo = createLogoDocument(file, siteId, logoFolder.getId());
        Document addedLogo = repositoryService.addDocument(logo, file);
        log.info("File storage initialization completed.");
        return addedLogo.getId();
    }

    private Folder createLogoFolder(Long siteId) {
        return repositoryService.addFolder(
                Folder.builder()
                        .title("Logo")
                        .parentFolderId(siteId)
                        .path(String.valueOf(siteId))
                        .siteId(siteId)
                        .build());
    }

    private Document createLogoDocument(MultipartFile file, Long siteId, Long folderId) {
        String originalFilename = file.getOriginalFilename();
        String extension = getExtensionByStringHandling(originalFilename).orElse("");
        return Document.builder()
                .title(originalFilename)
                .fileName(originalFilename)
                .extension(extension)
                .path(siteId + "/" + folderId)
                .folderId(folderId)
                .siteId(siteId)
                .build();
    }

    private Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    @SneakyThrows
    private void loadInitData(Long siteId) {
        log.info("Loading initial site data...");
        contentRepository.saveAll(contentCsvParser.getContentsFromCsv(siteId));
        pageRepository.saveAll(pageCsvParser.getPagesFromCsv(siteId));
        List<Menu> menus = menuRepository.saveAll(menuCsvParser.getMenusFromCsv(siteId));
        updateMenuParents(menus, siteId);
        log.info("Initial site data loaded.");
    }

    private void updateMenuParents(List<Menu> menus, Long siteId) {
        menus.forEach(menu -> {
            Long parentMenuId = menu.getParentMenuId();
            if (parentMenuId == 1) {
                menu.setParentMenuId(getParentMenuIdByTitle("About", siteId));
            } else if (parentMenuId == 5) {
                menu.setParentMenuId(getParentMenuIdByTitle("User Support", siteId));
            } else if (parentMenuId == 9) {
                menu.setParentMenuId(getParentMenuIdByTitle("Community Portals", siteId));
            } else if (parentMenuId == 21) {
                menu.setParentMenuId(getParentMenuIdByTitle("My workspace", siteId));
            }
            menuRepository.save(menu);
        });
    }

    private Long getParentMenuIdByTitle(String title, Long siteId) {
        Menu parentMenu = menuRepository.findByTitleAndSiteId(title, siteId);
        return parentMenu != null ? parentMenu.getId() : null;
    }

}
