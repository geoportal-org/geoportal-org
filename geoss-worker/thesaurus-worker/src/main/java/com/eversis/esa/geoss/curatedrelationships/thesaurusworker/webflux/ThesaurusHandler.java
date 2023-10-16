package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.webflux;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.EsaThesaurusLoader;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.earth.EarthThesaurusLoader;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.eosterm.EostermThesaurusLoader;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.ThesaurusJobModel;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.ThesaurusType;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.service.ThesaurusJobService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * The type Thesaurus handler.
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class ThesaurusHandler {

    private final ThesaurusJobService thesaurusJobService;

    private final EsaThesaurusLoader esaThesaurusLoader;

    private final EostermThesaurusLoader eostermThesaurusLoader;

    private final EarthThesaurusLoader earthThesaurusLoader;

    /**
     * Gets job.
     *
     * @param type the type
     * @return the job
     */
    public Mono<ThesaurusJobModel> getJob(String type) {
        log.info("type:{}", type);
        ThesaurusType thesaurusType = ThesaurusType.fromString(type);
        ThesaurusJobModel thesaurusJob = thesaurusJobService.get(thesaurusType);
        log.info("job:{}", thesaurusJob);
        return Mono.just(thesaurusJob);
    }

    /**
     * Run job mono.
     *
     * @param type the type
     * @return the mono
     */
    public Mono<ThesaurusJobModel> runJob(String type) {
        log.info("type:{}", type);
        final ThesaurusType thesaurusType = ThesaurusType.fromString(type);
        ThesaurusJobModel thesaurusJob = thesaurusJobService.get(thesaurusType);
        if (thesaurusJob.isPending()) {
            log.info("job:{}", thesaurusJob);
            return getJob(type);
        }
        ThesaurusJobModel thesaurusJobModel = thesaurusJobService.start(thesaurusType);
        loadJob(thesaurusType)
                .doFirst(() -> thesaurusJobService.started(thesaurusType))
                .doOnCancel(() -> thesaurusJobService.stopped(thesaurusType))
                .doOnEach(stringSignal -> thesaurusJobService.update(thesaurusType))
                .doOnComplete(() -> thesaurusJobService.complete(thesaurusType))
                .doOnError(throwable -> thesaurusJobService.fail(thesaurusType, throwable))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(o -> log.info("subscribe:{}", o), e -> log.error("error: " + e.getMessage(), e));
        log.info("job:{}", thesaurusJobModel);
        return getJob(type);
    }

    /**
     * Load job flux.
     *
     * @param thesaurusType the thesaurus type
     * @return the flux
     */
    public Flux<String> loadJob(ThesaurusType thesaurusType) {
        return switch (thesaurusType) {
            case ESA -> esaThesaurusLoader.load();
            case EOSTERM -> eostermThesaurusLoader.load();
            case EARTH -> earthThesaurusLoader.load();
            default -> throw new IllegalStateException("Unexpected value: " + thesaurusType);
        };
    }
}
