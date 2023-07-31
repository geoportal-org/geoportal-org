package com.eversis.esa.geoss.curatedrelationships.search.service.search;

import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQueryType;

import javax.validation.constraints.NotNull;

/**
 * The interface Search query service.
 */
public interface SearchQueryService {

    /**
     * Process search phrase search query.
     *
     * @param searchQuery the search query
     * @return the search query
     */
    SearchQuery processSearchPhrase(@NotNull SearchQuery searchQuery);

    /**
     * Gets search query type.
     *
     * @param phrase the phrase
     * @return the search query type
     */
    SearchQueryType getSearchQueryType(@NotNull String phrase);

}
