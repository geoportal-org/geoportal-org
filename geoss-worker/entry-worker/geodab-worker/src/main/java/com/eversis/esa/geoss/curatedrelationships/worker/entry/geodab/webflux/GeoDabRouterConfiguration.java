package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.webflux;

import org.springdoc.core.annotations.RouterOperation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * The type Geo dab router configuration.
 */
@Configuration
public class GeoDabRouterConfiguration {

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    /**
     * Gets job.
     *
     * @param jobHandler the job handler
     * @return the job
     */
    @RouterOperation(
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = JobHandler.class,
            beanMethod = "getJob"
    )
    @Bean
    RouterFunction<ServerResponse> getJob(JobHandler jobHandler) {
        return route()
                .GET(basePath + "/jobs/geodab", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(jobHandler.getJob(), Job.class)
                )
                .build();
    }

    /**
     * Run job router function.
     *
     * @param jobHandler the job handler
     * @return the router function
     */
    @RouterOperation(
            produces = MediaType.APPLICATION_JSON_VALUE,
            beanClass = JobHandler.class,
            beanMethod = "runJob"
    )
    @Bean
    RouterFunction<ServerResponse> runJob(JobHandler jobHandler) {
        return route()
                .POST(basePath + "/jobs/geodab", serverRequest -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(jobHandler.runJob(), JobExecution.class))
                .build();
    }
}
