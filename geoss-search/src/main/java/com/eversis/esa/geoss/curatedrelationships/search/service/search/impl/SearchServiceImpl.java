package com.eversis.esa.geoss.curatedrelationships.search.service.search.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.model.exception.ResourceNotFoundException;
import com.eversis.esa.geoss.curatedrelationships.search.service.search.SearchQueryService;
import com.eversis.esa.geoss.curatedrelationships.search.service.search.SearchService;
import com.eversis.esa.geoss.curatedrelationships.search.service.search.SearchStrategy;
import com.eversis.esa.geoss.curatedrelationships.search.service.search.SearchStrategyFactory;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * The type Search service.
 */
@Log4j2
@Service
@Validated
class SearchServiceImpl implements SearchService {

    private SearchStrategyFactory searchStrategyFactory;
    private SearchQueryService queryService;

    /**
     * Instantiates a new Search service.
     *
     * @param searchStrategyFactory the search strategy factory
     * @param queryService the query service
     */
    @Autowired
    public SearchServiceImpl(SearchStrategyFactory searchStrategyFactory, SearchQueryService queryService) {
        this.searchStrategyFactory = searchStrategyFactory;
        this.queryService = queryService;
    }

    @Override
    public FacetedPage<Entry> findResources(@NotNull SearchQuery query, @NotNull Pageable pageable) {
        SearchQuery processedQuery = processSearchQuery(query);
        if (log.isDebugEnabled()) {
            log.debug("Searching for CR resources entries, using query: {}, startIndex: {}, pageSize: {}, order: {}",
                    processedQuery, pageable.getStartIndex(), pageable.getPageSize(), pageable.getSort());
        }

        SearchStrategy searchStrategy = searchStrategyFactory.getSearchStrategy(query.getDataSource());
        return searchStrategy.search(processedQuery, pageable);
    }

    private SearchQuery processSearchQuery(@NotNull SearchQuery query) {
        return queryService.processSearchPhrase(query);
    }

    @Override
    public Page<Entry> findResourcesById(@NotNull Set<String> ids, @NotNull DataSource dataSource,
            @NotNull Pageable pageable) {
        if (log.isDebugEnabled()) {
            log.debug("Searching for CR resources entries, using ids: {}, startIndex: {}, pageSize: {}, order: {}",
                    ids, pageable.getStartIndex(), pageable.getPageSize(), pageable.getSort());
        }
        SearchStrategy searchStrategy = searchStrategyFactory.getSearchStrategy(dataSource);
        Page<Entry> entries = searchStrategy.findResourcesById(ids, pageable);
        if (ids.size() == 1 && !entries.hasContent()) {
            throw new ResourceNotFoundException(
                    "Unable to find resource with id: " + ids.stream().findFirst().orElse(""));
        }

        return entries;
    }
}
