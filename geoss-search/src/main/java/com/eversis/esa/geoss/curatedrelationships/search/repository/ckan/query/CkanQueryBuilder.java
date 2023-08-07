package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query;

import com.eversis.esa.geoss.curatedrelationships.search.model.Facets;
import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQueryType;
import com.eversis.esa.geoss.curatedrelationships.search.utils.StringUtils;

import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.stream.Collectors;

import static com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query.CkanQueryParameters.FACET_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query.CkanQueryParameters.FILTER_QUERY;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query.CkanQueryParameters.QUERY;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query.CkanQueryParameters.ROWS_COUNT;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query.CkanQueryParameters.START_INDEX;

/**
 * The type Ckan query builder.
 */
class CkanQueryBuilder {

    private UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("/package_search");

    /**
     * Query ckan query builder.
     *
     * @param queryType the query type
     * @param phrase the phrase
     * @return the ckan query builder
     */
    CkanQueryBuilder query(SearchQueryType queryType, String phrase) {
        String ckanPhrase;
        switch (queryType) {
            case KEYWORD:
                ckanPhrase = StringUtils.wrapWithDoubleQuotes(phrase);
                break;
            case FULL_TEXT:
            default:
                ckanPhrase = phrase;
                break;
        }
        urlBuilder.queryParam(QUERY, ckanPhrase);
        return this;
    }

    /**
     * Start index ckan query builder.
     *
     * @param startIndex the start index
     * @return the ckan query builder
     */
    CkanQueryBuilder startIndex(int startIndex) {
        urlBuilder.queryParam(START_INDEX, startIndex);
        return this;
    }

    /**
     * Rows ckan query builder.
     *
     * @param rows the rows
     * @return the ckan query builder
     */
    CkanQueryBuilder rows(int rows) {
        urlBuilder.queryParam(ROWS_COUNT, rows);
        return this;
    }

    /**
     * Filter query ckan query builder.
     *
     * @param filterQuery the filter query
     * @return the ckan query builder
     */
    CkanQueryBuilder filterQuery(String filterQuery) {
        urlBuilder.queryParam(FILTER_QUERY, filterQuery);
        return this;
    }

    /**
     * Facet fields ckan query builder.
     *
     * @param facetFields the facet fields
     * @return the ckan query builder
     */
    CkanQueryBuilder facetFields(Map<String, Facets> facetFields) {
        if (!CollectionUtils.isEmpty(facetFields)) {
            String jsonArray = "["
                               + facetFields.keySet()
                                       .stream()
                                       .map(StringUtils::wrapWithDoubleQuotes)
                                       .collect(Collectors.joining(","))
                               + "]";

            urlBuilder.queryParam(FACET_FIELD, jsonArray);
        }
        return this;
    }

    /**
     * Build string.
     *
     * @return the string
     */
    String build() {
        return urlBuilder.build().toUriString();
    }

}
