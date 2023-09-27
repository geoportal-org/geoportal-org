package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.eosterm;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model.ConceptDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model.TermDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.eosterm.service.EostermThesaurusService;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.service.ThesaurusDataStoreService;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * The type Eosterm thesaurus loader.
 */
@Log4j2
@Component
public class EostermThesaurusLoader {

    private static final int MAX_RETRY_COUNT = 2;
    private static final int RETRY_SECONDS_INTERVAL = 3;

    private final EostermThesaurusService thesaurusService;
    private final ThesaurusDataStoreService dataStoreService;

    /**
     * Instantiates a new Eosterm thesaurus loader.
     *
     * @param thesaurusService the thesaurus service
     * @param dataStoreService the data store service
     */
    @Autowired
    public EostermThesaurusLoader(
            EostermThesaurusService thesaurusService,
            ThesaurusDataStoreService dataStoreService) {
        this.thesaurusService = thesaurusService;
        this.dataStoreService = dataStoreService;
    }

    /**
     * Loads recursively concepts from EOSTERM Thesaurus and saves them in configured data store.
     *
     * @return the flux
     */
    public Flux<String> load() {
        log.info("Loading concepts from EOSTERM Thesaurus...");
        return thesaurusService.fetchTopTerms()
                .map(TermDto::getTermId)
                .expandDeep(this::loadTermAndItsSubTerms)
                .retryWhen(Retry.backoff(MAX_RETRY_COUNT, Duration.ofSeconds(RETRY_SECONDS_INTERVAL)))
                .delayElements(Duration.ofMillis(500))
                .doOnError(throwable -> log.error("Failed to load EOSTERM Thesaurus", throwable))
                .doOnComplete(() -> log.info("EOSTERM Thesaurus has been successfully loaded"))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Loads data about specified concept, saves it and loads uri of concepts, which one level deeper than currently
     * processed concept.
     *
     * @param termId currently processed concept's termId
     * @return flux of uris which are one level deeper in hierarchy than concept, which is currently processed
     */
    private Flux<String> loadTermAndItsSubTerms(String termId) {
        return thesaurusService.fetchConcept(termId)
                .doOnNext(this::saveConcept)
                .flatMapIterable(ConceptDto::getNarrowerConcepts)
                .map(TermDto::getTermId);
    }

    private void saveConcept(ConceptDto conceptDto) {
        if (!conceptDto.isMetaTerm()) {
            dataStoreService.saveConcept(conceptDto.toConcept());
        }
    }

}
