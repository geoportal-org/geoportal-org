package com.eversis.esa.geoss.contents.controller;

import com.eversis.esa.geoss.contents.domain.Site;
import com.eversis.esa.geoss.contents.service.SiteService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
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
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The type Site controller.
 */
@Log4j2
@RepositoryRestController("/site")
@ResponseBody
@Tag(name = "sites")
public class SiteController {

    private final SiteService siteService;
    private final EntityLinks entityLinks;

    /**
     * Instantiates a new Site controller.
     *
     * @param siteService the site service
     * @param entityLinks the entity links
     */
    public SiteController(SiteService siteService, EntityLinks entityLinks) {
        this.siteService = siteService;
        this.entityLinks = entityLinks;
    }

    /**
     * Create site entity model.
     *
     * @param files the files
     * @param model the model
     * @return the entity model
     */
    @RequestBody(
            content = @Content(
                    mediaType = "multipart/form-data",
                    schemaProperties = {
                            @SchemaProperty(
                                    name = "model",
                                    schema = @Schema(
                                            type = "object",
                                            implementation = Site.class
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
    @SneakyThrows
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Site> createSite(
            @Parameter(hidden = true)
            @RequestParam("files") MultipartFile[] files,
            @Parameter(hidden = true)
            @RequestParam("model") String model) {
        ObjectMapper mapper = new ObjectMapper();
        Site siteDTO = mapper.readValue(model, Site.class);

        validateUrl(siteDTO.getUrl());
        validateFileName(files[0]);

        Site createdSite = siteService.createSite(siteDTO, files[0]);
        return EntityModel.of(createdSite, siteLinks(createdSite));
    }

    /**
     * Delete site.
     *
     * @param id the id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSite(@PathVariable Long id) {
        siteService.deleteSite(id);
    }

    private List<Link> siteLinks(Site site) {
        if (site.getId() == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(
                entityLinks.linkToItemResource(Site.class, site.getId())
        );
    }

    private void validateUrl(String url) {
        if (siteService.existsByUrl(url)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "URL already exists: " + url);
        }

        if (!url.matches("^[a-zA-Z0-9-]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "URL can only contain alphanumeric characters and hyphen (-): " + url);
        }
    }

    private void validateFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename().trim();

        if (fileName.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "File name exceeds maximum length of 255 characters: " + fileName);
        }

        if (!fileName.matches("^[a-zA-Z0-9-_. ]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "File name can only contain alphanumeric characters, hyphens (-), underscores (_), dots (.)"
                    + " and space: " + fileName);
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
