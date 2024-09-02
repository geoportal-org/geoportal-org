package com.eversis.esa.geoss.contents.controller;

import com.eversis.esa.geoss.contents.domain.Document;
import com.eversis.esa.geoss.contents.domain.Site;
import com.eversis.esa.geoss.contents.service.RepositoryService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The type Document controller.
 */
@Log4j2
@RepositoryRestController("/document")
@ResponseBody
@Tag(name = "documents")
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
    @RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schemaProperties = {
                            @SchemaProperty(
                                    name = "model",
                                    schema = @Schema(
                                            type = "object",
                                            implementation = Document.class
                                    )
                            ),
                            @SchemaProperty(
                                    name = "files",
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    type = "string",
                                                    format = "binary"
                                            )
                                    )
                            )
                    }
            )
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Document> saveDocument(
            @Parameter(hidden = true)
            @RequestParam("files") MultipartFile[] files,
            @Parameter(hidden = true)
            @RequestParam("model") String model) {

        validateFileName(files[0]);

        Document savedDocument;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Document documentDTO = mapper.readValue(model, Document.class);
            repositoryService.validateFileNameUnique(documentDTO);
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

    private void validateFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename().trim();

        if (fileName.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "File name exceeds maximum length of 255 characters: " + fileName);
        }

        if (!fileName.matches("^[a-zA-Z0-9-_.]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "File name can only contain alphanumeric characters, hyphens (-), underscores (_), and dots (.): "
                            + fileName);
        }

        if (!StandardCharsets.UTF_8.newEncoder().canEncode(fileName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "File name is not in valid UTF-8 format: " + fileName);
        }

        if (fileName.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "File name cannot be empty.");
        }
    }

}
