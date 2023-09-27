package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config.sdg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The type Un sdg configuration.
 */
@Component
@ConfigurationProperties(prefix = "config.sdg.un")
@Getter
@Setter
public class UNSdgProperties {

    private String baseUrl;
    private String goalsEndpoint;
    private String organisationTitle;
    private String sourceTitle;
    private String sourceCode;

}
