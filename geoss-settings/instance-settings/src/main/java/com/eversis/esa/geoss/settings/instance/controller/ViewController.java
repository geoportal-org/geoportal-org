package com.eversis.esa.geoss.settings.instance.controller;

import com.eversis.esa.geoss.common.hateoas.PageMapper;
import com.eversis.esa.geoss.settings.instance.domain.View;
import com.eversis.esa.geoss.settings.instance.model.ViewOptionModel;
import com.eversis.esa.geoss.settings.instance.service.ViewService;

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
 * The type View controller.
 */
@Log4j2
@RequiredArgsConstructor
@RepositoryRestController("/views")
@ResponseBody
@Tag(name = "views")
public class ViewController {

    private final ViewService viewService;

    private final PageMapper pageMapper;

    private final EntityLinks entityLinks;

    /**
     * Head view options response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @Hidden
    @RequestMapping(method = RequestMethod.HEAD, path = "{id}/options")
    ResponseEntity<?> headViewOptions(@PathVariable Long id) {
        List<ViewOptionModel> viewOptions = viewService.getViewOptions(id);
        if (null == viewOptions) {
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        Links links = Links.of(viewOptionsLinks());
        headers.add(HttpHeaders.LINK, links.toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Gets view options.
     *
     * @param id the id
     * @return the view options
     */
    @GetMapping("{id}/options")
    CollectionModel<EntityModel<ViewOptionModel>> getViewOptions(@PathVariable Long id) {
        List<ViewOptionModel> viewOptions = viewService.getViewOptions(id);
        return pageMapper.toCollectionModel(viewOptions, this::viewOptionLinks, this::viewOptionsLinks);
    }

    /**
     * Delete view options.
     *
     * @param id the id
     */
    @DeleteMapping("{id}/options")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteViewOptions(@PathVariable Long id) {
        viewService.removeViewOptions(id);
    }

    /**
     * Post view option entity model.
     *
     * @param id the id
     * @param viewOptionModel the view option model
     * @return the entity model
     */
    @PostMapping("{id}/options")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<ViewOptionModel> postViewOption(@PathVariable Long id,
            @RequestBody @Valid ViewOptionModel viewOptionModel) {
        ViewOptionModel viewOption = viewService.addViewOption(id, viewOptionModel);
        return EntityModel.of(viewOption, viewOptionLinks(viewOption));
    }

    /**
     * Head view option response entity.
     *
     * @param id the id
     * @param optionId the option id
     * @return the response entity
     */
    @Hidden
    @RequestMapping(method = RequestMethod.HEAD, path = "{id}/options/{optionId}")
    ResponseEntity<?> headViewOption(@PathVariable Long id, @PathVariable Long optionId) {
        ViewOptionModel viewOptionModel = viewService.getViewOption(id, optionId);
        if (null == viewOptionModel) {
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        Links links = Links.of(viewOptionLinks(viewOptionModel));
        headers.add(HttpHeaders.LINK, links.toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Gets view option.
     *
     * @param id the id
     * @param optionId the option id
     * @return the view option
     */
    @GetMapping("{id}/options/{optionId}")
    EntityModel<ViewOptionModel> getViewOption(@PathVariable Long id, @PathVariable Long optionId) {
        ViewOptionModel viewOptionModel = viewService.getViewOption(id, optionId);
        return EntityModel.of(viewOptionModel, viewOptionLinks(viewOptionModel));
    }

    /**
     * Put view option entity model.
     *
     * @param id the id
     * @param optionId the option id
     * @param viewOptionModel the view option model
     * @return the entity model
     */
    @PutMapping("{id}/options/{optionId}")
    EntityModel<ViewOptionModel> putViewOption(@PathVariable Long id, @PathVariable Long optionId,
            @RequestBody @Valid ViewOptionModel viewOptionModel) {
        ViewOptionModel viewOption = viewService.setViewOption(id, optionId, viewOptionModel);
        return EntityModel.of(viewOption, viewOptionLinks(viewOption));
    }

    /**
     * Delete view option.
     *
     * @param id the id
     * @param optionId the option id
     */
    @DeleteMapping("{id}/options/{optionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteViewOption(@PathVariable Long id, @PathVariable Long optionId) {
        viewService.removeViewOption(id, optionId);
    }

    /**
     * Patch view option entity model.
     *
     * @param id the id
     * @param optionId the option id
     * @param viewOptionModel the view option model
     * @return the entity model
     */
    @PatchMapping("{id}/options/{optionId}")
    EntityModel<ViewOptionModel> patchViewOption(@PathVariable Long id, @PathVariable Long optionId,
            @RequestBody ViewOptionModel viewOptionModel) {
        ViewOptionModel viewOption = viewService.updateViewOption(id, optionId, viewOptionModel);
        return EntityModel.of(viewOption, viewOptionLinks(viewOption));
    }

    private List<Link> viewOptionsLinks() {
        final String requestUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        return Arrays.asList(
                Link.of(requestUri).withSelfRel(),
                entityLinks.linkToCollectionResource(View.class)
        );
    }

    private List<Link> viewOptionLinks(ViewOptionModel viewOptionModel) {
        if (viewOptionModel.getViewId() == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(
                entityLinks.linkToItemResource(View.class, viewOptionModel.getViewId()),
                linkTo(methodOn(ViewController.class).getViewOptions(viewOptionModel.getViewId())).withRel("options"),
                linkTo(methodOn(ViewController.class).getViewOption(viewOptionModel.getViewId(),
                        viewOptionModel.getId())).withRel("option")
        );
    }
}
