package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.webflux;

import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * The type Thesaurus router configuration.
 */
@Log4j2
@Configuration
public class ThesaurusRouterConfiguration {

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    /**
     * Gets esa thesaurus.
     *
     * @param thesaurusHandler the thesaurus handler
     * @return the esa thesaurus
     */
    @RouterOperation(
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = ThesaurusHandler.class,
            beanMethod = "getEsaThesaurus"
    )
    @Bean
    RouterFunction<ServerResponse> getEsaThesaurus(ThesaurusHandler thesaurusHandler) {
        return route()
                .GET(basePath + "/jobs/esa", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(thesaurusHandler.getEsaThesaurus(), ThesaurusJobModel.class)
                )
                .build();
    }

    /**
     * Load esa thesaurus router function.
     *
     * @param thesaurusHandler the thesaurus handler
     * @return the router function
     */
    @RouterOperation(
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = ThesaurusHandler.class,
            beanMethod = "loadEsaThesaurus"
    )
    @Bean
    RouterFunction<ServerResponse> loadEsaThesaurus(ThesaurusHandler thesaurusHandler) {
        return route()
                .POST(basePath + "/jobs/esa", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(thesaurusHandler.loadEsaThesaurus(), ThesaurusJobModel.class))
                .build();
    }

    /**
     * Gets eosterm thesaurus.
     *
     * @param thesaurusHandler the thesaurus handler
     * @return the eosterm thesaurus
     */
    @RouterOperation(
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = ThesaurusHandler.class,
            beanMethod = "getEostermThesaurus"
    )
    @Bean
    RouterFunction<ServerResponse> getEostermThesaurus(ThesaurusHandler thesaurusHandler) {
        return route()
                .GET(basePath + "/jobs/eosterm", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(thesaurusHandler.getEostermThesaurus(), ThesaurusJobModel.class)
                )
                .build();
    }

    /**
     * Load eosterm thesaurus router function.
     *
     * @param thesaurusHandler the thesaurus handler
     * @return the router function
     */
    @RouterOperation(
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = ThesaurusHandler.class,
            beanMethod = "loadEostermThesaurus"
    )
    @Bean
    RouterFunction<ServerResponse> loadEostermThesaurus(ThesaurusHandler thesaurusHandler) {
        return route()
                .POST(basePath + "/jobs/eosterm", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(thesaurusHandler.loadEostermThesaurus(), ThesaurusJobModel.class))
                .build();
    }

    /**
     * Gets earth thesaurus.
     *
     * @param thesaurusHandler the thesaurus handler
     * @return the earth thesaurus
     */
    @RouterOperation(
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = ThesaurusHandler.class,
            beanMethod = "getEarthThesaurus"
    )
    @Bean
    RouterFunction<ServerResponse> getEarthThesaurus(ThesaurusHandler thesaurusHandler) {
        return route()
                .GET(basePath + "/jobs/earth", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(thesaurusHandler.getEarthThesaurus(), ThesaurusJobModel.class)
                )
                .build();
    }

    /**
     * Load earth thesaurus router function.
     *
     * @param thesaurusHandler the thesaurus handler
     * @return the router function
     */
    @RouterOperation(
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = ThesaurusHandler.class,
            beanMethod = "loadEarthThesaurus"
    )
    @Bean
    RouterFunction<ServerResponse> loadEarthThesaurus(ThesaurusHandler thesaurusHandler) {
        return route()
                .POST(basePath + "/jobs/earth", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(thesaurusHandler.loadEarthThesaurus(), ThesaurusJobModel.class))
                .build();
    }

    /**
     * Gets all.
     *
     * @param thesaurusHandler the thesaurus handler
     * @return the all
     */
    @RouterOperation(
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = ThesaurusHandler.class,
            beanMethod = "getAll"
    )
    @Bean
    RouterFunction<ServerResponse> getAll(ThesaurusHandler thesaurusHandler) {
        return route()
                .GET(basePath + "/jobs/all", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(thesaurusHandler.getAll(), ThesaurusJobModel.class)
                )
                .build();
    }

    /**
     * Load all router function.
     *
     * @param thesaurusHandler the thesaurus handler
     * @return the router function
     */
    @RouterOperation(
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = ThesaurusHandler.class,
            beanMethod = "loadAll"
    )
    @Bean
    RouterFunction<ServerResponse> loadAll(ThesaurusHandler thesaurusHandler) {
        return route()
                .POST(basePath + "/jobs/all", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(thesaurusHandler.loadAll(), ThesaurusJobModel.class))
                .build();
    }
}
