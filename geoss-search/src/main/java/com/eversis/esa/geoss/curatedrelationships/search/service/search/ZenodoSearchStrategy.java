package com.eversis.esa.geoss.curatedrelationships.search.service.search;

import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.repository.CRRepository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * The type Zenodo search strategy.
 */
@Component("zenodoSearchStrategy")
class ZenodoSearchStrategy implements SearchStrategy {

    private final CRRepository<Entry> dataRepository;

    /**
     * Instantiates a new Zenodo search strategy.
     *
     * @param dataRepository the data repository
     */
    public ZenodoSearchStrategy(@Qualifier("entryZenodoRepository") CRRepository<Entry> dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public FacetedPage<Entry> search(@NotNull SearchQuery query, @NotNull Pageable pageable) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public Page<Entry> findResourcesById(@NotNull Set<String> ids, @NotNull Pageable pageable) {
        return dataRepository.findResourcesById(ids, pageable);
    }
}
