package com.eversis.esa.geoss.contents.controller;

import com.eversis.esa.geoss.contents.service.RepositoryService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Document content controller.
 */
@Slf4j
@RestController
@RequestMapping("/rest/document")
public class DocumentContentController {

    private final RepositoryService repositoryService;

    /**
     * Instantiates a new Document content controller.
     *
     * @param repositoryService the repository service
     */
    public DocumentContentController(final RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    /**
     * Gets document content.
     *
     * @param id the id
     * @return the document content
     */
    @GetMapping("{id}/content")
    public ResponseEntity<Resource> getDocumentContent(@PathVariable Long id) {
        Resource file = repositoryService.getDocumentContent(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
