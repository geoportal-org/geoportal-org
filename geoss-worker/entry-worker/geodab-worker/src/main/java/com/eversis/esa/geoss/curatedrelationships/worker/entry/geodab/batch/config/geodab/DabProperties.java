package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The type Dab configuration.
 */
@Component
@ConfigurationProperties(prefix = "config.dab")
@Getter
@Setter
public class DabProperties {

    private String entryCodePrefix;
    private String sourceTitle;
    private String sourceCode;

}
