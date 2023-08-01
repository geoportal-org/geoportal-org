package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo;

import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.PageImpl;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.model.exception.SearchFailureException;
import com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.mapper.ZenodoMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.query.ZenodoQueryFactory;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * The type Entry zenodo repository.
 */
@Log4j2
@Repository("entryZenodoRepository")
class EntryZenodoRepository extends ZenodoRepository<Entry> {

    /**
     * Instantiates a new Entry zenodo repository.
     *
     * @param client the client
     * @param queryFactory the query factory
     * @param mapper the mapper
     */
    public EntryZenodoRepository(
            @Qualifier("zenodoClient") WebClient client,
            ZenodoQueryFactory queryFactory,
            ZenodoMapper<Entry> mapper) {
        super(client, queryFactory, mapper);
    }

    @Override
    public FacetedPage<Entry> findResources(@NotNull SearchQuery query, @NotNull Pageable pageable) {
        throw new IllegalStateException("Not implemented");
    }

    @Override
    public Page<Entry> findResourcesById(@NotNull Set<String> ids, @NotNull Pageable pageable) {
        try {
            return Flux
                    .merge(Flux.fromIterable(ids))
                    .flatMap(this::findResource)
                    .onErrorContinue(WebClientResponseException.class, this::handleMissingResource)
                    .collectList()
                    .map(PageImpl::new)
                    .block();
        } catch (Exception e) {
            throw new SearchFailureException("Failed to find resource on external server. " + e.getMessage(), e);
        }
    }

    private void handleMissingResource(Throwable throwable, Object object) {
        if (throwable instanceof WebClientResponseException) {
            log.warn("Failed to fetch resource object. HTTP Code: "
                     + ((WebClientResponseException) throwable).getRawStatusCode());
        } else {
            log.error("Failed to fetch resource object.", throwable);
        }
    }

}
