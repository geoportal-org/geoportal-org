package com.eversis.esa.geoss.settings.instance.controller;

import com.eversis.esa.geoss.settings.common.hateoas.PageMapper;
import com.eversis.esa.geoss.settings.instance.domain.Catalog;
import com.eversis.esa.geoss.settings.instance.model.CatalogOptionModel;
import com.eversis.esa.geoss.settings.instance.service.CatalogService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The type Catalog controller.
 */
@Log4j2
@RequiredArgsConstructor
@RepositoryRestController("/catalogs")
@ResponseBody
@Tag(name = "catalogs")
public class CatalogController {

    private final CatalogService catalogService;

    private final PageMapper pageMapper;

    private final EntityLinks entityLinks;

    /**
     * Head catalog options response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @Hidden
    @RequestMapping(method = RequestMethod.HEAD, path = "{id}/options")
    ResponseEntity<?> headCatalogOptions(@PathVariable Long id) {
        List<CatalogOptionModel> catalogOptions = catalogService.getCatalogOptions(id);
        if (null == catalogOptions) {
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        Links links = Links.of(catalogOptionsLinks());
        headers.add(HttpHeaders.LINK, links.toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Gets catalog options.
     *
     * @param id the id
     * @return the catalog options
     */
    @GetMapping("{id}/options")
    CollectionModel<EntityModel<CatalogOptionModel>> getCatalogOptions(@PathVariable Long id) {
        List<CatalogOptionModel> catalogOptions = catalogService.getCatalogOptions(id);
        return pageMapper.toCollectionModel(catalogOptions, this::catalogOptionLinks, this::catalogOptionsLinks);
    }

    /**
     * Delete catalog options.
     *
     * @param id the id
     */
    @DeleteMapping("{id}/options")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCatalogOptions(@PathVariable Long id) {
        catalogService.removeCatalogOptions(id);
    }

    /**
     * Post catalog option entity model.
     *
     * @param id the id
     * @param catalogOptionModel the catalog option model
     * @return the entity model
     */
    @PostMapping("{id}/options")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<CatalogOptionModel> postCatalogOption(@PathVariable Long id,
            @RequestBody @Valid CatalogOptionModel catalogOptionModel) {
        CatalogOptionModel catalogOption = catalogService.addCatalogOption(id, catalogOptionModel);
        return EntityModel.of(catalogOption, catalogOptionLinks(catalogOption));
    }

    /**
     * Head catalog option response entity.
     *
     * @param id the id
     * @param optionId the option id
     * @return the response entity
     */
    @Hidden
    @RequestMapping(method = RequestMethod.HEAD, path = "{id}/options/{optionId}")
    ResponseEntity<?> headCatalogOption(@PathVariable Long id, @PathVariable Long optionId) {
        CatalogOptionModel catalogOptionModel = catalogService.getCatalogOption(id, optionId);
        if (null == catalogOptionModel) {
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        Links links = Links.of(catalogOptionLinks(catalogOptionModel));
        headers.add(HttpHeaders.LINK, links.toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Gets catalog option.
     *
     * @param id the id
     * @param optionId the option id
     * @return the catalog option
     */
    @GetMapping("{id}/options/{optionId}")
    EntityModel<CatalogOptionModel> getCatalogOption(@PathVariable Long id, @PathVariable Long optionId) {
        CatalogOptionModel catalogOptionModel = catalogService.getCatalogOption(id, optionId);
        return EntityModel.of(catalogOptionModel, catalogOptionLinks(catalogOptionModel));
    }

    /**
     * Put catalog option entity model.
     *
     * @param id the id
     * @param optionId the option id
     * @param catalogOptionModel the catalog option model
     * @return the entity model
     */
    @PutMapping("{id}/options/{optionId}")
    EntityModel<CatalogOptionModel> putCatalogOption(@PathVariable Long id, @PathVariable Long optionId,
            @RequestBody @Valid CatalogOptionModel catalogOptionModel) {
        CatalogOptionModel catalogOption = catalogService.setCatalogOption(id, optionId, catalogOptionModel);
        return EntityModel.of(catalogOption, catalogOptionLinks(catalogOption));
    }

    /**
     * Delete catalog option.
     *
     * @param id the id
     * @param optionId the option id
     */
    @DeleteMapping("{id}/options/{optionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCatalogOption(@PathVariable Long id, @PathVariable Long optionId) {
        catalogService.removeCatalogOption(id, optionId);
    }

    /**
     * Patch catalog option entity model.
     *
     * @param id the id
     * @param optionId the option id
     * @param catalogOptionModel the catalog option model
     * @return the entity model
     */
    @PatchMapping("{id}/options/{optionId}")
    EntityModel<CatalogOptionModel> patchCatalogOption(@PathVariable Long id, @PathVariable Long optionId,
            @RequestBody CatalogOptionModel catalogOptionModel) {
        CatalogOptionModel catalogOption = catalogService.updateCatalogOption(id, optionId, catalogOptionModel);
        return EntityModel.of(catalogOption, catalogOptionLinks(catalogOption));
    }

    private List<Link> catalogOptionsLinks() {
        final String requestUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        return Arrays.asList(
                Link.of(requestUri).withSelfRel(),
                entityLinks.linkToCollectionResource(Catalog.class)
        );
    }

    private List<Link> catalogOptionLinks(CatalogOptionModel catalogOptionModel) {
        if (catalogOptionModel.getCatalogId() == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(
                entityLinks.linkToItemResource(Catalog.class, catalogOptionModel.getCatalogId()),
                linkTo(methodOn(CatalogController.class).getCatalogOptions(catalogOptionModel.getCatalogId())).withRel(
                        "options"),
                linkTo(methodOn(CatalogController.class).getCatalogOption(catalogOptionModel.getCatalogId(),
                        catalogOptionModel.getId())).withRel("option")
        );
    }
}
