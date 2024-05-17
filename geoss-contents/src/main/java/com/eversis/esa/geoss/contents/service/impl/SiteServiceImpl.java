package com.eversis.esa.geoss.contents.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.eversis.esa.geoss.contents.configuration.RepositoryProperties;
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
import com.eversis.esa.geoss.contents.service.SiteService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the SiteService interface.
 * This service manages the creation and deletion of sites.
 */
@Slf4j
@Service
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;
    private final ContentRepository contentRepository;
    private final DocumentRepository documentRepository;
    private final FolderRepository folderRepository;
    private final MenuRepository menuRepository;
    private final PageRepository pageRepository;
    private final ContentCsvParser contentCsvParser;
    private final PageCsvParser pageCsvParser;
    private final MenuCsvParser menuCsvParser;
    private final Path rootDirectory;

    /**
     * Constructor for the SiteServiceImpl class.
     *
     * @param siteRepository The repository for Site entities.
     * @param contentRepository The repository for Content entities.
     * @param documentRepository The repository for Document entities.
     * @param folderRepository The repository for Folder entities.
     * @param menuRepository The repository for Menu entities.
     * @param pageRepository The repository for Page entities.
     * @param contentCsvParser The parser for Content CSV files.
     * @param pageCsvParser The parser for Page CSV files.
     * @param menuCsvParser The parser for Menu CSV files.
     * @param repositoryProperties The properties for the repository.
     */
    public SiteServiceImpl(SiteRepository siteRepository, ContentRepository contentRepository,
            DocumentRepository documentRepository, FolderRepository folderRepository, MenuRepository menuRepository,
            PageRepository pageRepository, ContentCsvParser contentCsvParser, PageCsvParser pageCsvParser,
            MenuCsvParser menuCsvParser,
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
        siteRepository.deleteById(siteId);
        contentRepository.deleteBySiteId(siteId);
        documentRepository.deleteBySiteId(siteId);
        folderRepository.deleteBySiteId(siteId);
        menuRepository.deleteBySiteId(siteId);
        pageRepository.deleteBySiteId(siteId);
        log.info("Site with id: {} deleted", siteId);
    }

    /**
     * Creates a new site and loads initial data from CSV files.
     *
     * @param site The site to be created.
     * @return The created site.
     */
    @Override
    @Transactional
    public Site createSite(Site site) {
        log.info("Creating site: {}", site);
        Site createdSite = siteRepository.save(site);
        log.info("Site created: {}", createdSite);
        loadInitData(createdSite.getId());
        return createdSite;
    }

    /**
     * Loads initial data for a site from CSV files.
     *
     * @param siteId The ID of the site for which to load data.
     */
    @SneakyThrows
    private void loadInitData(Long siteId) {
        log.info("Loading init site data...");
        contentRepository.saveAll(contentCsvParser.getContentsFromCsv(siteId));
        pageRepository.saveAll(pageCsvParser.getPagesFromCsv(siteId));
        menuRepository.saveAll(menuCsvParser.getMenusFromCsv(siteId));
        log.info("Init site data loaded.");
    }

}
