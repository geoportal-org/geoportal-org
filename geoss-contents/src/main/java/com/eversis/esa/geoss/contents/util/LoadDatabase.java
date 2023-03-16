package com.eversis.esa.geoss.contents.util;

import com.eversis.esa.geoss.contents.domain.Content;
import com.eversis.esa.geoss.contents.domain.Menu;
import com.eversis.esa.geoss.contents.domain.Page;
import com.eversis.esa.geoss.contents.repository.ContentRepository;
import com.eversis.esa.geoss.contents.repository.DocumentRepository;
import com.eversis.esa.geoss.contents.repository.FolderRepository;
import com.eversis.esa.geoss.contents.repository.MenuRepository;
import com.eversis.esa.geoss.contents.repository.PageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Load database.
 */
@Slf4j
@Configuration
public class LoadDatabase {

    /**
     * Init database command line runner.
     *
     * @param contentRepository the content repository
     * @param documentRepository the document repository
     * @param folderRepository the folder repository
     * @param pageRepository the page repository
     * @return the command line runner
     */
    @Bean
    CommandLineRunner initDatabase(ContentRepository contentRepository, DocumentRepository documentRepository,
            FolderRepository folderRepository, PageRepository pageRepository, MenuRepository menuRepository) {

        return args -> {
            contentRepository.save(new Content(1L, "Content title example 1", "Content example 1", true));

            contentRepository.save(new Content(2L, "Content title example 2", "Content example geoss", true));

            contentRepository.save(new Content(3L, "Content title example 3", "Content example geoss 2", false));

            contentRepository.findAll().forEach(content -> log.info("Preloaded " + content));

            pageRepository.save(
                    new Page(1L, "Page title example 1", "Page example", 0L, "page-title-example-1", false));

            pageRepository.save(
                    new Page(2L, "Page title example 2", "Page example 2", 1L, "page-title-example-2", true));

            pageRepository.save(
                    new Page(3L, "Page title example 3", "Page example 3", 2L, "page-title-example-3", true));

            pageRepository.findAll().forEach(page -> log.info("Preloaded " + page));

            menuRepository.save(new Menu(1L, "Menu 1 level 0", "Image title 1",
                    "gpp.devel.esaportal.eu/contents/rest/document/4/content", "gpp.devel.esaportal.eu/release-notes",
                    1, 0L, 0));

            menuRepository.save(new Menu(2L, "Menu 2 level 0", "Image title 2",
                    "gpp.devel.esaportal.eu/contents/rest/document/1/content", "gpp.devel.esaportal.eu/about-us", 2, 0L,
                    0));

            menuRepository.save(new Menu(3L, "Menu 1 level 1", "Image title 3",
                    "gpp.devel.esaportal.eu/contents/rest/document/2/content", "gpp.devel.esaportal.eu/help", 1, 1L,
                    1));

            menuRepository.findAll().forEach(menu -> log.info("Preloaded " + menu));

        };
    }
}
