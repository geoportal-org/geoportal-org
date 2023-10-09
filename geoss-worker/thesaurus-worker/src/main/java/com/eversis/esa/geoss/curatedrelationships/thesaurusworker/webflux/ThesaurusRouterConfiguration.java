package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.webflux;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.ThesaurusType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @RouterOperation(
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = ThesaurusHandler.class,
            beanMethod = "getJob",
            operation = @Operation(
                    operationId = "getJob",
                    parameters = {
                            @Parameter(in = ParameterIn.PATH,
                                       name = "type",
                                       schema = @Schema(implementation = ThesaurusType.class))
                    }
            )
    )
    @Bean
    RouterFunction<ServerResponse> getJob(ThesaurusHandler thesaurusHandler) {
        return route()
                .GET(basePath + "/jobs/{type}", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(thesaurusHandler.getJob(serverRequest.pathVariable("type")), ThesaurusJobModel.class)
                )
                .build();
    }

    @RouterOperation(
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = ThesaurusHandler.class,
            beanMethod = "runJob",
            operation = @Operation(
                    operationId = "runJob",
                    parameters = {
                            @Parameter(in = ParameterIn.PATH,
                                       name = "type",
                                       schema = @Schema(implementation = ThesaurusType.class))
                    }
            )
    )
    @Bean
    RouterFunction<ServerResponse> runJob(ThesaurusHandler thesaurusHandler) {
        return route()
                .POST(basePath + "/jobs/{type}", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(thesaurusHandler.runJob(serverRequest.pathVariable("type")), ThesaurusJobModel.class))
                .build();
    }

    @RouterOperation(
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = ThesaurusHandler.class,
            beanMethod = "loadJob",
            operation = @Operation(
                    operationId = "loadJob",
                    parameters = {
                            @Parameter(in = ParameterIn.PATH,
                                       name = "type",
                                       schema = @Schema(implementation = ThesaurusType.class))
                    }
            )
    )
    @Bean
    RouterFunction<ServerResponse> loadJob(ThesaurusHandler thesaurusHandler) {
        return route()
                .POST(basePath + "/jobs/{type}/load", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_NDJSON)
                        .body(thesaurusHandler.loadJob(serverRequest.pathVariable("type")), String.class))
                // .body(thesaurusHandler.loadJob(serverRequest.pathVariable("type")), ThesaurusJobModel.class))
                .build();
        // ;
    }
}
