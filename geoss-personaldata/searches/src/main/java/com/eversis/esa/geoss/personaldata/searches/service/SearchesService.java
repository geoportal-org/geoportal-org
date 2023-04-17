package com.eversis.esa.geoss.personaldata.searches.service;

/**
 * The interface Searches service.
 */
public interface SearchesService {

    /**
     * Create highlighted searches from saved searches long.
     *
     * @param savedSearchesId the saved searches id
     * @param name the name
     * @return the higlighted searches id
     */
    Long createHighlightedSearchesFromSavedSearches(Long savedSearchesId, String name);
}
