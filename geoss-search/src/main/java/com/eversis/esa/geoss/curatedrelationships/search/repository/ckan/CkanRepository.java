package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan;

import com.eversis.esa.geoss.curatedrelationships.search.model.Facets;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.exception.SearchFailureException;
import com.eversis.esa.geoss.curatedrelationships.search.repository.CRRepository;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.CkanResponseMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.base.CkanResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

/**
 * The type Ckan repository.
 *
 * @param <T> the type parameter
 * @param <S> the type parameter
 */
@Slf4j
abstract class CkanRepository<T, S> implements CRRepository<T> {

    private final WebClient client;
    private final CkanResponseMapper<T, S> responseMapper;

    /**
     * Instantiates a new Ckan repository.
     *
     * @param client the client
     * @param responseMapper the response mapper
     */
    public CkanRepository(
            WebClient client,
            CkanResponseMapper<T, S> responseMapper) {
        this.client = client;
        this.responseMapper = responseMapper;
    }

    /**
     * Search page.
     *
     * @param pageable the pageable
     * @param query the query
     * @return the page
     */
    public Page<T> search(Pageable pageable, String query) {
        CkanResponse<S> searchResponse = search(query);
        return responseMapper.mapPackageSearchResponse(searchResponse, pageable);
    }

    /**
     * Search faceted page.
     *
     * @param pageable the pageable
     * @param query the query
     * @param facetFields the facet fields
     * @return the faceted page
     */
    public FacetedPage<T> search(Pageable pageable, String query, Map<String, Facets> facetFields) {
        CkanResponse<S> searchResponse = search(query);
        return responseMapper.mapFacetedPackageSearchResponse(searchResponse, pageable, facetFields);
    }

    protected CkanResponse<S> search(String uriQuery) {
        try {
            if (log.isTraceEnabled()) {
                log.trace("CKAN query : {}", uriQuery);
            }
            CkanResponse<S> searchResponse = client
                    .get()
                    .uri(uriQuery)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<CkanResponse<S>>() { })
                    .timeout(Duration.ofSeconds(10))
                    .block();

            if (log.isTraceEnabled()) {
                log.trace("CKAN response : {}", searchResponse);
            }
            return searchResponse;
        } catch (Exception ex) {
            log.error("Failed to get CKAN response from external server");
            throw new SearchFailureException("Failed to get CKAN response from external server", ex);
        }
    }

}
