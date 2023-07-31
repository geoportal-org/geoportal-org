package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * The type Ckan search result.
 *
 * @param <T> the type parameter
 */
@Data
public class CkanSearchResult<T> {

    private long count;
    private List<T> results;

    @JsonProperty("search_facets")
    protected Map<String, CkanFacetWrapper> searchFacets;

}
