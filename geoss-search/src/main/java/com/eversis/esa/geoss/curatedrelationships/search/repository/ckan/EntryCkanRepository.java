package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan;

import com.eversis.esa.geoss.curatedrelationships.search.model.Facets;
import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.CkanResponseMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query.CkanQueryFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The type Entry ckan repository.
 */
@Repository("ckanRepository")
class EntryCkanRepository extends CkanRepository<Entry, Map> {

    private static final Map<String, Facets> FACET_FIELDS;

    static {
        FACET_FIELDS = new HashMap<>();
        FACET_FIELDS.put("tags", Facets.KEYWORDS);
        FACET_FIELDS.put("organization", Facets.ORGANISATION);
        FACET_FIELDS.put("format", Facets.FORMAT);
    }

    private final CkanQueryFactory queryFactory;

    /**
     * Instantiates a new Entry ckan repository.
     *
     * @param queryFactory the query factory
     * @param responseMapper the response mapper
     * @param client the client
     */
    public EntryCkanRepository(
            CkanQueryFactory queryFactory,
            CkanResponseMapper<Entry, Map> responseMapper,
            @Qualifier("amerigeossCkanClient") WebClient client) {
        super(client, responseMapper);
        this.queryFactory = queryFactory;
    }

    @Override
    public FacetedPage<Entry> findResources(SearchQuery searchParameters, Pageable pageable) {
        String query = queryFactory.buildSearchQuery(
                searchParameters,
                pageable,
                FACET_FIELDS);
        return search(pageable, query, FACET_FIELDS);
    }

    @Override
    public Page<Entry> findResourcesById(@NotNull Set<String> ids, @NotNull Pageable pageable) {
        String query = queryFactory.buildIdQuery(
                ids,
                pageable);
        return search(pageable, query);
    }

}
