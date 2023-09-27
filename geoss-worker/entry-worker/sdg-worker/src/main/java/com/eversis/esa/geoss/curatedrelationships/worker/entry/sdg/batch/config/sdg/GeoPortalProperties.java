package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config.sdg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The type Geo portal configuration.
 */
@Component
@ConfigurationProperties(prefix = "config.geoportal")
@Getter
@Setter
public class GeoPortalProperties {

    private String baseUrl;
    private String baseImagesEndpoint;
    private String baseJsonwsEndpoint;

}
