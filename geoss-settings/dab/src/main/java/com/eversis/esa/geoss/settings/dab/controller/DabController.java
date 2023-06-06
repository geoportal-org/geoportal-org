package com.eversis.esa.geoss.settings.dab.controller;

import com.eversis.esa.geoss.settings.common.hateoas.PageMapper;
import com.eversis.esa.geoss.settings.dab.service.DabService;
import com.eversis.esa.geoss.settings.instance.domain.Catalog;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collections;
import java.util.List;

/**
 * The type Dab controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/dabs")
@ResponseBody
@Tag(name = "dabs")
public class DabController {

    private final DabService dabService;

    private final PageMapper pageMapper;

    private final EntityLinks entityLinks;

    /**
     * Search paged model.
     *
     * @param pageable the pageable
     * @return the paged model
     */
    @GetMapping("/catalogs")
    PagedModel<EntityModel<Catalog>> search(@ParameterObject @PageableDefault Pageable pageable) {
        Page<Catalog> catalogs = dabService.getCatalogs(pageable);
        return pageMapper.toPagedModel(catalogs, Catalog.class, this::catalogLinks, this::catalogLinks);
    }

    private List<Link> catalogLinks(Catalog model) {
        if (model == null) {
            return Collections.emptyList();
        }
        return List.of(
                entityLinks.linkToItemResource(Catalog.class, model.getId())
        );
    }

    private List<Link> catalogLinks() {
        final String requestUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        return List.of(
                Link.of(requestUri).withSelfRel(),
                entityLinks.linkToCollectionResource(Catalog.class)
        );
    }
}
