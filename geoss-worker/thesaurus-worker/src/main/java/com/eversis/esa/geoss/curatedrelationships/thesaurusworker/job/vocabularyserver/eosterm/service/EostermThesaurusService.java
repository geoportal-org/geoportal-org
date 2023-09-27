package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.eosterm.service;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.VocabularyServerService;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model.ConceptDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model.TermDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.query.VocabularyServerQueryFactory;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.eosterm.config.EostermThesaurusProperties;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.ThesaurusType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Eosterm thesaurus service.
 */
@Service
public class EostermThesaurusService extends VocabularyServerService {

    private final EostermThesaurusProperties configuration;

    /**
     * Instantiates a new Eosterm thesaurus service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     * @param configuration the configuration
     */
    @Autowired
    public EostermThesaurusService(
            @Qualifier("vocabularyServerThesaurusWebClient") WebClient webClient,
            VocabularyServerQueryFactory queryFactory,
            EostermThesaurusProperties configuration) {
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
        return super.fetchConcept(ThesaurusType.EOSTERM, configuration.getBaseUri(), termId);
    }
}
