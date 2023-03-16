package com.eversis.esa.geoss.contents.util;

import com.eversis.esa.geoss.contents.domain.Content;
import com.eversis.esa.geoss.contents.domain.Document;
import com.eversis.esa.geoss.contents.domain.Folder;
import com.eversis.esa.geoss.contents.domain.Page;
import com.eversis.esa.geoss.contents.repository.ContentRepository;
import com.eversis.esa.geoss.contents.repository.DocumentRepository;
import com.eversis.esa.geoss.contents.repository.FolderRepository;
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
            FolderRepository folderRepository, PageRepository pageRepository) {

        return args -> {
            contentRepository.save(new Content(1L, "Content title example 1","Content example 1",true));

            contentRepository.save(new Content(2L, "Content title example 2","Content example geoss",true));

            contentRepository.save(new Content(3L, "Content title example 3","Content example geoss 2",false));

            contentRepository.findAll().forEach(content -> log.info("Preloaded " + content));


            pageRepository.save(new Page(1L,"Page title example 1","Page example", 0L, "page-title-example-1", false));

            pageRepository.save(new Page(2L,"Page title example 2","Page example 2", 1L, "page-title-example-2", true));

            pageRepository.save(new Page(3L,"Page title example 3","Page example 3", 2L, "page-title-example-3", true));

            pageRepository.findAll().forEach(page -> log.info("Preloaded " + page));

            documentRepository.findAll().forEach(document -> log.info("Preloaded " + document));

        };
    }
}
