package com.eversis.esa.geoss.curated.elasticsearch.controller;

import com.eversis.esa.geoss.curated.elasticsearch.service.ElasticsearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Reindex entry controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/elasticsearch")
@ResponseBody
@Tag(name = "elasticsearch")
public class ReindexEntryController {

    private final ElasticsearchService elasticsearchService;

    /**
     * Reindex all.
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reindex/entries/all")
    public void reindexAll() {
        log.info("Reindex all entries");
        elasticsearchService.reindexEntries();
    }

    /**
     * Reindex by type.
     *
     * @param type the type
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reindex/entries/type/{type}")
    public void reindexByType(@PathVariable String type) {
        log.info("Reindex entries by type: {}", type);
        elasticsearchService.reindexEntriesByType(type);
    }

    /**
     * Reindex by data source.
     *
     * @param dataSource the data source
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reindex/entries/dataSource/{dataSource}")
    public void reindexByDataSource(@PathVariable String dataSource) {
        log.info("Reindex entries by data source: {}", dataSource);
        elasticsearchService.reindexEntriesByDataSource(dataSource);
    }

    /**
     * Clear all.
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/reindex/entries/clear")
    public void clearAll() {
        log.info("Clear all entries");
        elasticsearchService.clearEntries();
    }

}
