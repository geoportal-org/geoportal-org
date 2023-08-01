package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.Facets;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Facet;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.CompoundTermFacet;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.FacetedPageImpl;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.PageImpl;
import com.eversis.esa.geoss.curatedrelationships.search.model.exception.SearchFailureException;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.CkanResponseMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.CkanResultMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.base.CkanFacetWrapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.base.CkanResponse;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.base.CkanSearchResult;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The type Base ckan response mapper.
 *
 * @param <T> the type parameter
 * @param <S> the type parameter
 */
@Log4j2
abstract class BaseCkanResponseMapper<T, S> implements CkanResponseMapper<T, S> {

    protected CkanResultMapper<T, S> resultMapper;

    /**
     * Instantiates a new Base ckan response mapper.
     *
     * @param resultMapper the result mapper
     */
    public BaseCkanResponseMapper(CkanResultMapper<T, S> resultMapper) {
        this.resultMapper = resultMapper;
    }

    @Override
    public Page<T> mapPackageSearchResponse(CkanResponse<S> searchResponse, Pageable pageable) {
        if (!searchResponse.isSuccess()) {
            throw new SearchFailureException();
        }
        CkanSearchResult<S> searchResult = searchResponse.getResult();
        long totalCount = searchResult.getCount();
        List<T> elements = searchResult.getResults()
                .stream()
                .map(this::mapResult)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new PageImpl<>(elements, pageable, totalCount);
    }

    @Override
    public FacetedPage<T> mapFacetedPackageSearchResponse(CkanResponse<S> searchResponse, Pageable pageable,
            Map<String, Facets> facetFields) {
        Page<T> page = mapPackageSearchResponse(searchResponse, pageable);
        Map<String, List<Facet>> facets = collectFacets(searchResponse.getResult().getSearchFacets(), facetFields);

        return new FacetedPageImpl<>(page.getContent(), pageable, page.getTotalElements(), facets);
    }

    private T mapResult(S searchResult) {
        try {
            return resultMapper.mapToObject(searchResult);
        } catch (IOException ex) {
            log.error("Error occurred during parsing of search results", ex);
            throw new SearchFailureException("Unable to process result from CKAN : ", ex);
        }
    }

    private Map<String, List<Facet>> collectFacets(
            Map<String, CkanFacetWrapper> ckanFacetResults,
            Map<String, Facets> facetFields) {
        return ckanFacetResults
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> facetFields.get(e.getKey()).getName(),
                        e -> e.getValue().getItems()
                                .stream()
                                .map(ckanFacet -> new CompoundTermFacet(ckanFacet.getName(), ckanFacet.getDisplayName(),
                                        ckanFacet.getCount()))
                                .collect(Collectors.toList())
                ));
    }
}
