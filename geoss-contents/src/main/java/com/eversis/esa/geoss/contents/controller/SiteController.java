package com.eversis.esa.geoss.contents.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import jakarta.validation.Valid;

import com.eversis.esa.geoss.contents.domain.Site;
import com.eversis.esa.geoss.contents.service.SiteService;
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
     * @param site the site
     * @return the entity model
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Site> createSite(@Valid @RequestBody Site site) {
        Site createdSite = siteService.createSite(site);
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

}
