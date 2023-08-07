package com.eversis.esa.geoss.curatedrelationships.search.service.search;

import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.repository.CRRepository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * The type Wikipedia search strategy.
 */
@Component("wikipediaSearchStrategy")
class WikipediaSearchStrategy implements SearchStrategy {

    private final CRRepository<Entry> dataRepository;

    /**
     * Instantiates a new Wikipedia search strategy.
     *
     * @param dataRepository the data repository
     */
    public WikipediaSearchStrategy(@Qualifier("geossRepository") CRRepository<Entry> dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public FacetedPage<Entry> search(@NotNull SearchQuery query, @NotNull Pageable pageable) {
        return dataRepository.findResources(query, pageable);
    }

    @Override
    public Page<Entry> findResourcesById(@NotNull Set<String> ids, @NotNull Pageable pageable) {
        return dataRepository.findResourcesById(ids, pageable);
    }
}
