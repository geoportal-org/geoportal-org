package com.eversis.esa.geoss.curatedrelationships.search.service.search;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;

import java.util.Set;
import javax.validation.constraints.NotNull;

public interface SearchService {

    /**
     * Search for curated relationships resources. Business rules may be applied to the search options.
     *
     * @param query filter parameters
     * @param pageable pagination information
     */
    FacetedPage<Entry> findResources(@NotNull SearchQuery query, @NotNull Pageable pageable);

    /**
     * Search for curated relationships resources by their id. Business rules may be applied to the search options.
     *
     * @param ids list of entry's ids filter parameters
     * @param dataSource source which should
     * @param pageable pagination information
     */
    Page<Entry> findResourcesById(@NotNull Set<String> ids, @NotNull DataSource dataSource, @NotNull Pageable pageable);

}
