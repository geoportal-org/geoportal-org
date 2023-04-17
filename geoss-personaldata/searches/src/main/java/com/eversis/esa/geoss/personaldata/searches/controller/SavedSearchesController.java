package com.eversis.esa.geoss.personaldata.searches.controller;

import com.eversis.esa.geoss.personaldata.searches.domain.HighlightedSearches;
import com.eversis.esa.geoss.personaldata.searches.domain.SavedSearches;
import com.eversis.esa.geoss.personaldata.searches.model.SavedSearchesHighlightedModel;
import com.eversis.esa.geoss.personaldata.searches.service.SearchesService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.List;

/**
 * The type Saved searches controller.
 */
@Log4j2
@RequiredArgsConstructor
@RepositoryRestController("/saved-searches")
@ResponseBody
@Tag(name = "saved-searches")
public class SavedSearchesController {

    private final SearchesService searchesService;

    private final EntityLinks entityLinks;

    /**
     * Create highlighted searches from saved searches.
     *
     * @param savedSearchesId the saved searches id
     * @param savedSearchesHighlightedModel the saved searches highlighted model
     * @return the entity model
     */
    @PostMapping("/{id}/highlighted")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<SavedSearchesHighlightedModel> createHighlightedSearchesFromSavedSearches(
            @PathVariable("id") Long savedSearchesId,
            @RequestBody SavedSearchesHighlightedModel savedSearchesHighlightedModel) {
        Long highlightedSearchesId = searchesService.createHighlightedSearchesFromSavedSearches(savedSearchesId,
                savedSearchesHighlightedModel.getName());
        return EntityModel.of(savedSearchesHighlightedModel, searchesLinks(savedSearchesId, highlightedSearchesId));
    }

    private List<Link> searchesLinks(Long savedSearchesId, Long highlightedSearchesId) {
        return Arrays.asList(
                entityLinks.linkToItemResource(SavedSearches.class, savedSearchesId),
                entityLinks.linkToItemResource(HighlightedSearches.class, highlightedSearchesId)
        );
    }
}
