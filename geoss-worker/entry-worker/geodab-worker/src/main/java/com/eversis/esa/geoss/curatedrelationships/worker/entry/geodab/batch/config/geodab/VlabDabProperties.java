package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The type Vlab dab configuration.
 */
@Component
@ConfigurationProperties(prefix = "config.dab.vlab")
@Getter
@Setter
public class VlabDabProperties {

    private String baseUrl;
    private String apiToken;

    private String workflowsEndpoint;
    private String runsEndpoint;

    private RunConfiguration runs;

    /**
     * The type Run configuration.
     */
    @Getter
    @Setter
    public static class RunConfiguration {

        private String entryCodePrefix;

    }

}
