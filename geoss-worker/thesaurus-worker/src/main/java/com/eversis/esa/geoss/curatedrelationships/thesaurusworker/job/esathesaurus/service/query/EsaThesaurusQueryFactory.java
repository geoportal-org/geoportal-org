package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.query;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.config.EsaThesaurusProperties;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The type Esa thesaurus query factory.
 */
@Component
public class EsaThesaurusQueryFactory {

    private final EsaThesaurusProperties configuration;

    /**
     * Instantiates a new Esa thesaurus query factory.
     *
     * @param configuration the configuration
     */
    public EsaThesaurusQueryFactory(EsaThesaurusProperties configuration) {
        this.configuration = configuration;
    }

    /**
     * Creates full uri for provided uri, which identifies concept.
     *
     * @param dataUri uri for concept
     * @return the string
     */
    public String createUriQuery(String dataUri) {
        return UriComponentsBuilder.fromHttpUrl(configuration.getBaseUri())
                .queryParam("uri", dataUri)
                .queryParam("format", "application/json")
                .build(true).toUriString();
    }

}
