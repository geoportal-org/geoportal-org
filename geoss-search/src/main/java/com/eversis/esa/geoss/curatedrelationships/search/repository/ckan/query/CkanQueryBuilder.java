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

class CkanQueryBuilder {

    private UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("/package_search");

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

    CkanQueryBuilder startIndex(int startIndex) {
        urlBuilder.queryParam(START_INDEX, startIndex);
        return this;
    }

    CkanQueryBuilder rows(int rows) {
        urlBuilder.queryParam(ROWS_COUNT, rows);
        return this;
    }

    CkanQueryBuilder filterQuery(String filterQuery) {
        urlBuilder.queryParam(FILTER_QUERY, filterQuery);
        return this;
    }

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

    String build() {
        return urlBuilder.build().toUriString();
    }

}
