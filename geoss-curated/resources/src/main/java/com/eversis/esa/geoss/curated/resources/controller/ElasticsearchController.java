package com.eversis.esa.geoss.curated.resources.controller;

import com.eversis.esa.geoss.curated.resources.service.ElasticsearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Elasticsearch controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/elasticsearch")
@ResponseBody
@Tag(name = "elasticsearch")
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;

    /**
     * Index entry.
     *
     * @param entryId the entry id
     */
    @PreAuthorize("hasAnyRole('RESOURCE_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{entryId}")
    public void indexEntry(@PathVariable long entryId) {
        log.info("Index entry");
        elasticsearchService.indexEntry(entryId);
    }

    /**
     * Update index entry.
     *
     * @param entryId the entry id
     */
    @PreAuthorize("hasAnyRole('RESOURCE_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{entryId}")
    public void updateIndexEntry(@PathVariable long entryId) {
        log.info("Update index entry");
        elasticsearchService.updateEntry(entryId);
    }

    /**
     * Remove entry from index.
     *
     * @param entryId the entry id
     */
    @PreAuthorize("hasAnyRole('RESOURCE_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{entryId}")
    public void removeEntryFromIndex(@PathVariable long entryId) {
        log.info("Remove entry from index");
        elasticsearchService.removeEntryFromIndex(entryId);
    }

}
