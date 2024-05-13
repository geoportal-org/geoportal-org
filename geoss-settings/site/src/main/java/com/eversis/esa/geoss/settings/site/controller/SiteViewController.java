package com.eversis.esa.geoss.settings.site.controller;

import com.eversis.esa.geoss.common.hateoas.PageMapper;
import com.eversis.esa.geoss.common.hateoas.SimpleLinkBuilder;
import com.eversis.esa.geoss.settings.instance.domain.View;
import com.eversis.esa.geoss.settings.instance.model.ViewModel;
import com.eversis.esa.geoss.settings.instance.service.ViewService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.BaseUri;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The type Site view controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController
@ResponseBody
@Tags({@Tag(name = "site"), @Tag(name = "views")})
public class SiteViewController {

    private final ViewService viewService;

    private final PageMapper pageMapper;

    private final EntityLinks entityLinks;

    private final BaseUri baseUri;

    /**
     * Head site views response entity.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the response entity
     */
    @Hidden
    @RequestMapping(path = "/sites/{siteId}/views", method = RequestMethod.HEAD)
    ResponseEntity<?> headSiteViews(@PathVariable Long siteId, Pageable pageable) {
        Page<ViewModel> views = viewService.getSiteViews(siteId, pageable);
        if (null == views) {
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        Links links = Links.of(viewLinks(siteId));
        headers.add(HttpHeaders.LINK, links.toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Gets site views.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the site views
     */
    @GetMapping("/sites/{siteId}/views")
    PagedModel<EntityModel<ViewModel>> getSiteViews(@Parameter(example = "0") @PathVariable Long siteId,
            @ParameterObject @PageableDefault Pageable pageable) {
        Page<ViewModel> views = viewService.getSiteViews(siteId, pageable);
        return pageMapper.toPagedModel(views, ViewModel.class, this::viewLinks,
                () -> viewLinks(siteId));
    }

    /**
     * Delete site views.
     *
     * @param siteId the site id
     */
    @DeleteMapping("/sites/{siteId}/views")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSiteViews(@PathVariable Long siteId) {
        viewService.deleteSiteViews(siteId);
    }

    private List<Link> viewLinks(Long siteId) {
        final String basePath = baseUri.getUri().toString();
        return List.of(
                entityLinks.linkToCollectionResource(View.class),
                pageMapper.linkToCollectionResource(
                        SimpleLinkBuilder.linkTo(basePath, "/sites/", String.valueOf(siteId), "/views"), "siteViews")
        );
    }

    private List<Link> viewLinks(ViewModel viewModel) {
        if (viewModel.getId() == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(
                entityLinks.linkToItemResource(View.class, viewModel.getId()).withSelfRel(),
                entityLinks.linkToItemResource(View.class, viewModel.getId())
        );
    }
}
