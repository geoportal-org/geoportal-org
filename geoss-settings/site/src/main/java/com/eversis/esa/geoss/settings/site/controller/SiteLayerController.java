package com.eversis.esa.geoss.settings.site.controller;

import com.eversis.esa.geoss.common.hateoas.PageMapper;
import com.eversis.esa.geoss.common.hateoas.SimpleLinkBuilder;
import com.eversis.esa.geoss.settings.instance.domain.Layer;
import com.eversis.esa.geoss.settings.instance.model.LayerModel;
import com.eversis.esa.geoss.settings.instance.service.LayerService;

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
 * The type Site layer controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController
@ResponseBody
@Tags({@Tag(name = "site"), @Tag(name = "default-layers")})
public class SiteLayerController {

    private final LayerService layerService;

    private final PageMapper pageMapper;

    private final EntityLinks entityLinks;

    private final BaseUri baseUri;

    /**
     * Head site layers response entity.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the response entity
     */
    @Hidden
    @RequestMapping(path = "/sites/{siteId}/layers", method = RequestMethod.HEAD)
    ResponseEntity<?> headSiteLayers(@PathVariable Long siteId, Pageable pageable) {
        Page<LayerModel> layers = layerService.getSiteLayers(siteId, pageable);
        if (null == layers) {
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        Links links = Links.of(layerLinks(siteId));
        headers.add(HttpHeaders.LINK, links.toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Gets site layers.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the site layers
     */
    @GetMapping("/sites/{siteId}/layers")
    PagedModel<EntityModel<LayerModel>> getSiteLayers(@Parameter(example = "0") @PathVariable Long siteId,
            @ParameterObject @PageableDefault Pageable pageable) {
        Page<LayerModel> layers = layerService.getSiteLayers(siteId, pageable);
        return pageMapper.toPagedModel(layers, LayerModel.class, this::layerLinks,
                () -> layerLinks(siteId));
    }

    /**
     * Delete site layers.
     *
     * @param siteId the site id
     */
    @DeleteMapping("/sites/{siteId}/layers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSiteLayers(@PathVariable Long siteId) {
        layerService.deleteSiteLayers(siteId);
    }

    private List<Link> layerLinks(Long siteId) {
        final String basePath = baseUri.getUri().toString();
        return List.of(
                entityLinks.linkToCollectionResource(Layer.class),
                pageMapper.linkToCollectionResource(
                        SimpleLinkBuilder.linkTo(basePath, "/sites/", String.valueOf(siteId), "/layers"), "siteLayers")
        );
    }

    private List<Link> layerLinks(LayerModel layerModel) {
        if (layerModel.getId() == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(
                entityLinks.linkToItemResource(Layer.class, layerModel.getId()).withSelfRel(),
                entityLinks.linkToItemResource(Layer.class, layerModel.getId())
        );
    }
}
