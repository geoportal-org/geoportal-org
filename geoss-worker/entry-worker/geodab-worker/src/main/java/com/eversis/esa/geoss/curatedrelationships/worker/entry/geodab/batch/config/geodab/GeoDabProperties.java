package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The type Geo dab configuration.
 */
@Component
@ConfigurationProperties(prefix = "config.dab.geodab")
@Getter
@Setter
public class GeoDabProperties {

    private String baseUrl;
    private String organisation;
    private String ecosystemsEndpoint;
    private String protectedAreasEndpoint;
    private String storylinesEndpoint;
    private String workflowsEndpoint;

    private String protectedAreasTransferOptionTitle;
    private String storylinesTransferOptionTitle;
}
