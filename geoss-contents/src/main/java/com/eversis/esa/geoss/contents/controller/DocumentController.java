package com.eversis.esa.geoss.contents.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.eversis.esa.geoss.contents.domain.Document;
import com.eversis.esa.geoss.contents.service.RepositoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Document controller.
 */
@Log4j2
@RepositoryRestController("/document")
@ResponseBody
public class DocumentController {

    private final RepositoryService repositoryService;
    private final EntityLinks entityLinks;

    /**
     * Instantiates a new Document controller.
     *
     * @param repositoryService the repository service
     * @param entityLinks the entity links
     */
    public DocumentController(
            final RepositoryService repositoryService,
            final EntityLinks entityLinks) {
        this.repositoryService = repositoryService;
        this.entityLinks = entityLinks;
    }

    /**
     * Save document entity model.
     *
     * @param files the files
     * @param model the model
     * @return the entity model
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Document> saveDocument(@RequestParam("files") MultipartFile[] files,
            @RequestParam("model") String model) {
        Document savedDocument;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Document documentDTO = mapper.readValue(model, Document.class);
            savedDocument = repositoryService.addDocument(documentDTO, files[0]);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return EntityModel.of(savedDocument, documentLinks(savedDocument));
    }

    /**
     * Delete document.
     *
     * @param id the id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteDocument(@PathVariable Long id) {
        repositoryService.removeDocument(id);
    }

    private List<Link> documentLinks(Document document) {
        if (document.getId() == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(
                entityLinks.linkToItemResource(Document.class, document.getId())
        );
    }
}
