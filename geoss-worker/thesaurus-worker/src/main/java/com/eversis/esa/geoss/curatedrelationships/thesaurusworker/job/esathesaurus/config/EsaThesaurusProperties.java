package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * The type Esa thesaurus configuration.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "thesaurus.esa")
public class EsaThesaurusProperties {

    private String baseUri;
    private List<String> topConceptsUris;

}
