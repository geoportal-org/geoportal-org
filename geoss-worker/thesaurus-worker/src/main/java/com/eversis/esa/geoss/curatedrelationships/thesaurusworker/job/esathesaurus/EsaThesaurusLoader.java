package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.config.EsaThesaurusProperties;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.EsaThesaurusService;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.model.BaseConceptDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.model.ConceptDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.service.ThesaurusDataStoreService;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * The type Esa thesaurus loader.
 */
@Log4j2
@Component
public class EsaThesaurusLoader {

    private static final int MAX_RETRY_COUNT = 2;
    private static final int RETRY_SECONDS_INTERVAL = 3;

    private final EsaThesaurusProperties configuration;
    private final EsaThesaurusService esaThesaurusService;
    private final ThesaurusDataStoreService dataStoreService;

    /**
     * Instantiates a new Esa thesaurus loader.
     *
     * @param configuration the configuration
     * @param esaThesaurusService the esa thesaurus service
     * @param dataStoreService the data store service
     */
    @Autowired
    public EsaThesaurusLoader(EsaThesaurusProperties configuration,
            EsaThesaurusService esaThesaurusService,
            ThesaurusDataStoreService dataStoreService) {
        this.configuration = configuration;
        this.esaThesaurusService = esaThesaurusService;
        this.dataStoreService = dataStoreService;
    }

    /**
     * Loads recursively concepts from ESA Thesaurus and saves them in configured data store.
     *
     * @return the flux
     */
    public Flux<String> load() {
        log.info("Loading concepts from ESA Thesaurus...");
        return Flux.fromIterable(configuration.getTopConceptsUris())
                .expandDeep(this::loadEntryAndItsSubEntriesUris)
                .delayElements(Duration.ofSeconds(1))
                .retryWhen(Retry.backoff(MAX_RETRY_COUNT, Duration.ofSeconds(RETRY_SECONDS_INTERVAL)))
                .doOnError(throwable -> log.error("Failed to load ESA Thesaurus", throwable))
                .doOnComplete(() -> log.info("ESA Thesaurus has been successfully loaded"))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Loads data about specified concept, saves it and loads uri of concepts, which one level deeper than currently
     * processed concept.
     *
     * @param currentUri currently processed concept's uri
     * @return flux of uris which are one level deeper in hierarchy than concept, which is currently processed
     */
    private Flux<String> loadEntryAndItsSubEntriesUris(String currentUri) {
        return esaThesaurusService.fetchConcept(currentUri)
                .doOnNext(conceptDto -> dataStoreService.saveConcept(conceptDto.toConcept()))
                .flatMapIterable(ConceptDto::getNarrowerConcepts)
                .map(BaseConceptDto::getUri)
                .filter(uri -> !uri.equals(currentUri));
    }

}
