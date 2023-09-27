package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.earth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Earth thesaurus configuration.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "thesaurus.earth")
public class EarthThesaurusProperties {

    private String baseUri;

}
