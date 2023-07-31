package com.eversis.esa.geoss.curatedrelationships.search.service.search;

import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;

import java.util.Set;
import javax.validation.constraints.NotNull;

public interface SearchStrategy {

    /**
     * Search for curated relationships resources using specified strategy. Each strategy should be bound to each datasource.
     *
     * @param query query filter parameters
     * @param pageable pagination information
     */
    FacetedPage<Entry> search(@NotNull SearchQuery query, @NotNull Pageable pageable);

    /**
     * Search for curated relationships resources by their id. Business rules may be applied to the search options.
     *
     * @param ids set of entry's ids filter parameters
     * @param pageable pagination information
     */
    Page<Entry> findResourcesById(@NotNull Set<String> ids, @NotNull Pageable pageable);

}
