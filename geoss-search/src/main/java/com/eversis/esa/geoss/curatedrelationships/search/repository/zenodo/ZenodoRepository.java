package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo;

import com.eversis.esa.geoss.curatedrelationships.search.repository.CRRepository;
import com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.mapper.ZenodoMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.model.ZenodoResult;
import com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.query.ZenodoQueryFactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * The type Zenodo repository.
 *
 * @param <T> the type parameter
 */
@Slf4j
abstract class ZenodoRepository<T> implements CRRepository<T> {

    protected final ZenodoQueryFactory queryFactory;
    protected final ZenodoMapper<T> mapper;
    private final WebClient client;

    /**
     * Instantiates a new Zenodo repository.
     *
     * @param client the client
     * @param queryFactory the query factory
     * @param mapper the mapper
     */
    public ZenodoRepository(WebClient client, ZenodoQueryFactory queryFactory,
            ZenodoMapper<T> mapper) {
        this.client = client;
        this.queryFactory = queryFactory;
        this.mapper = mapper;
    }

    protected Mono<T> findResource(String id) {
        String query = queryFactory.buildIdQuery(id);
        return findResource(query, ZenodoResult.class)
                .map(mapper::mapSearchResult)
                .timeout(Duration.ofSeconds(10));
    }

    private <S> Mono<S> findResource(String uriQuery, Class<S> typeParameterClass) {
        if (log.isTraceEnabled()) {
            log.trace("Zenodo query : {}", uriQuery);
        }
        return client
                .get()
                .uri(uriQuery)
                .retrieve()
                .bodyToMono(typeParameterClass)
                .doOnNext(searchResponse -> {
                    if (log.isTraceEnabled()) {
                        log.trace("Zenodo response : {}", searchResponse);
                    }
                });
    }

}
