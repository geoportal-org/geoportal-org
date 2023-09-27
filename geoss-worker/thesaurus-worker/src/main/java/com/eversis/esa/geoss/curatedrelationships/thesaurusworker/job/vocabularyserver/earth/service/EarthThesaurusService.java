package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.earth.service;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.VocabularyServerService;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model.ConceptDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model.TermDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.query.VocabularyServerQueryFactory;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.earth.config.EarthThesaurusProperties;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.ThesaurusType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Earth thesaurus service.
 */
@Service
public class EarthThesaurusService extends VocabularyServerService {

    private final EarthThesaurusProperties configuration;

    /**
     * Instantiates a new Earth thesaurus service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     * @param configuration the configuration
     */
    @Autowired
    public EarthThesaurusService(
            @Qualifier("vocabularyServerThesaurusWebClient") WebClient webClient,
            VocabularyServerQueryFactory queryFactory,
            EarthThesaurusProperties configuration) {
        super(webClient, queryFactory);
        this.configuration = configuration;
    }

    /**
     * Fetches top terms from vocabulary using provided common URI.
     *
     * @return the flux
     */
    public Flux<TermDto> fetchTopTerms() {
        return super.fetchTopTerms(configuration.getBaseUri());
    }

    /**
     * Downloads and parses concept basing on provided termId from external datasource.
     *
     * @param termId the term id
     * @return the mono
     */
    public Mono<ConceptDto> fetchConcept(String termId) {
        return super.fetchConcept(ThesaurusType.EARTH, configuration.getBaseUri(), termId);
    }
}
