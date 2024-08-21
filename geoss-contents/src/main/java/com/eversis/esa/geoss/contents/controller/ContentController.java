package com.eversis.esa.geoss.contents.controller;

import com.eversis.esa.geoss.contents.domain.Content;
import com.eversis.esa.geoss.contents.service.ContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.util.Collections;
import java.util.List;

/**
 * The type Content controller.
 */
@Log4j2
@RepositoryRestController("/content")
@ResponseBody
@Tag(name = "contents")
public class ContentController {

    private final ContentService contentService;
    private final EntityLinks entityLinks;

    /**
     * Instantiates a new Content controller.
     *
     * @param contentService the content service
     * @param entityLinks    the entity links
     */
    public ContentController(
            final ContentService contentService, EntityLinks entityLinks) {
        this.contentService = contentService;
        this.entityLinks = entityLinks;
    }

    /**
     * Delete by ids in.
     *
     * @param ids the ids
     */
    @DeleteMapping("deleteByIdsIn")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteByIdsIn(@RequestParam List<Long> ids) {
        contentService.deleteByIdsIn(ids);
    }

    /**
     * Update content entity model.
     *
     * @param contentId  the content id
     * @param contentDto the content dto
     * @return the entity model
     */
    @PutMapping("/{contentId}")
    @ResponseStatus(HttpStatus.OK)
    EntityModel<Content> updateContent(@PathVariable long contentId, @RequestBody @Valid Content contentDto) {
        Content updatedContent = contentService.updateContent(contentId, contentDto);
        return EntityModel.of(updatedContent, contentLinks(updatedContent));
    }

    private List<Link> contentLinks(Content content) {
        if (content.getId() == null) {
            return Collections.emptyList();
        }
        return List.of(
                entityLinks.linkToItemResource(Content.class, content.getId())
        );
    }

}
