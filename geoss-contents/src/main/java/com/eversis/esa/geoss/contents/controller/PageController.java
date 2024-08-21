package com.eversis.esa.geoss.contents.controller;

import com.eversis.esa.geoss.contents.domain.Page;
import com.eversis.esa.geoss.contents.service.PageService;
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
 * The type Page controller.
 */
@Log4j2
@RepositoryRestController("/page")
@ResponseBody
@Tag(name = "pages")
public class PageController {

    private final PageService pageService;
    private final EntityLinks entityLinks;

    /**
     * Instantiates a new Page controller.
     *
     * @param pageService the page service
     * @param entityLinks the entity links
     */
    public PageController(
            final PageService pageService, EntityLinks entityLinks) {
        this.pageService = pageService;
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
        pageService.deleteByIdsIn(ids);
    }

    /**
     * Update page entity model.
     *
     * @param pageId  the page id
     * @param pageDto the page dto
     * @return the entity model
     */
    @PutMapping("/{pageId}")
    @ResponseStatus(HttpStatus.OK)
    EntityModel<Page> updatePaget(@PathVariable long pageId, @RequestBody @Valid Page pageDto) {
        Page updatedPage = pageService.updatePage(pageId, pageDto);
        return EntityModel.of(updatedPage, pageLinks(updatedPage));
    }

    private List<Link> pageLinks(Page page) {
        if (page.getId() == null) {
            return Collections.emptyList();
        }
        return List.of(
                entityLinks.linkToItemResource(Page.class, page.getId())
        );
    }

}
