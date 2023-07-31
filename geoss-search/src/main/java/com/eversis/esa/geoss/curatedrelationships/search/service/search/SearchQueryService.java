package com.eversis.esa.geoss.curatedrelationships.search.service.search;

import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQueryType;

import javax.validation.constraints.NotNull;

public interface SearchQueryService {

    SearchQuery processSearchPhrase(@NotNull SearchQuery searchQuery);

    SearchQueryType getSearchQueryType(@NotNull String phrase);

}
