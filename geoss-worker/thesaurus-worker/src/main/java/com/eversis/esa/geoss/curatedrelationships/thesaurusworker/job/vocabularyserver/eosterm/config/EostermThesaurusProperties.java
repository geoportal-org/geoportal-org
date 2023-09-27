package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.eosterm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Eosterm thesaurus configuration.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "thesaurus.eosterm")
public class EostermThesaurusProperties {

    private String baseUri;

}
