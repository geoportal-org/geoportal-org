package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config.sdg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The type Sdg configuration.
 */
@Component
@ConfigurationProperties(prefix = "config.sdg")
@Getter
@Setter
public class SdgProperties {

    private String defaultLogo;

    private String entryCodePrefix;

    private boolean fetchMissingEntries;
}
