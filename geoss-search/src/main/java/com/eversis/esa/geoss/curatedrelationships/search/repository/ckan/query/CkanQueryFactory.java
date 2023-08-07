package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query;

import com.eversis.esa.geoss.curatedrelationships.search.model.Facets;
import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * The type Ckan query factory.
 */
@Component
public class CkanQueryFactory {

    /**
     * Build id query string.
     *
     * @param ids the ids
     * @param pageable the pageable
     * @return the string
     */
    public String buildIdQuery(@NotNull Iterable<String> ids, @NotNull Pageable pageable) {
        CkanQueryBuilder queryBuilder = new CkanQueryBuilder();
        CkanFilterQueryBuilder filterQueryBuilder = new CkanFilterQueryBuilder();

        filterQueryBuilder.ids(ids);

        queryBuilder
                .startIndex(pageable.getStartIndex())
                .rows(pageable.getPageSize())
                .filterQuery(filterQueryBuilder.build());

        return queryBuilder.build();
    }

    /**
     * Build search query string.
     *
     * @param searchParameters the search parameters
     * @param pageable the pageable
     * @return the string
     */
    public String buildSearchQuery(@NotNull SearchQuery searchParameters, @NotNull Pageable pageable) {
        CkanQueryBuilder queryBuilder = new CkanQueryBuilder();

        searchParameters.getOptionalPhrase()
                .ifPresent(phrase -> queryBuilder.query(searchParameters.getQueryType(), phrase));
        CkanFilterQueryBuilder filterQueryBuilder = createFilterQueryBuilder(searchParameters);

        queryBuilder.startIndex(pageable.getStartIndex())
                .rows(pageable.getPageSize())
                .filterQuery(filterQueryBuilder.build());

        return queryBuilder.build();
    }

    /**
     * Build search query string.
     *
     * @param searchParameters the search parameters
     * @param pageable the pageable
     * @param facetFields the facet fields
     * @return the string
     */
    public String buildSearchQuery(@NotNull SearchQuery searchParameters, @NotNull Pageable pageable,
            @NotNull Map<String, Facets> facetFields) {
        CkanQueryBuilder queryBuilder = new CkanQueryBuilder();

        CkanFilterQueryBuilder filterQueryBuilder = createFilterQueryBuilder(searchParameters);
        searchParameters.getOptionalPhrase()
                .ifPresent(phrase -> queryBuilder.query(searchParameters.getQueryType(), phrase));

        queryBuilder.startIndex(pageable.getStartIndex())
                .rows(pageable.getPageSize())
                .filterQuery(filterQueryBuilder.build())
                .facetFields(facetFields);

        return queryBuilder.build();
    }

    private CkanFilterQueryBuilder createFilterQueryBuilder(@NotNull SearchQuery searchParameters) {
        CkanFilterQueryBuilder filterQueryBuilder = new CkanFilterQueryBuilder();

        searchParameters.getOptionalDateRange().ifPresent(filterQueryBuilder::dateRange);
        searchParameters.getOptionalFormat().ifPresent(filterQueryBuilder::format);
        searchParameters.getOptionalKeyword().ifPresent(filterQueryBuilder::keyword);
        searchParameters.getOptionalOrganizationName().ifPresent(filterQueryBuilder::organization);

        return filterQueryBuilder;
    }

}
