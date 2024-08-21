package com.eversis.esa.geoss.contents.controller;

import com.eversis.esa.geoss.contents.domain.Folder;
import com.eversis.esa.geoss.contents.service.RepositoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The type Folder controller.
 */
@Log4j2
@RepositoryRestController("/folder")
@ResponseBody
@Tag(name = "folders")
public class FolderController {

    private final RepositoryService repositoryService;
    private final EntityLinks entityLinks;

    /**
     * Instantiates a new Folder controller.
     *
     * @param repositoryService the repository service
     * @param entityLinks the entity links
     */
    public FolderController(
            final RepositoryService repositoryService,
            final EntityLinks entityLinks) {
        this.repositoryService = repositoryService;
        this.entityLinks = entityLinks;
    }

    /**
     * Save folder entity model.
     *
     * @param folder the folder
     * @return the entity model
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Folder> saveFolder(@Valid @RequestBody Folder folder) {
        Folder savedFolder = repositoryService.addFolder(folder);
        return EntityModel.of(savedFolder, folderLinks(savedFolder));
    }

    /**
     * Delete folder.
     *
     * @param id the id
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteFolder(@PathVariable Long id) {
        repositoryService.removeFolder(id);
    }

    private List<Link> folderLinks(Folder folder) {
        if (folder.getId() == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(
                entityLinks.linkToItemResource(Folder.class, folder.getId())
        );
    }
}
