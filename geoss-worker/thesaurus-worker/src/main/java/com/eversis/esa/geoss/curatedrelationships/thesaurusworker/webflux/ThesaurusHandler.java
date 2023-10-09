package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.webflux;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.EsaThesaurusLoader;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.earth.EarthThesaurusLoader;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.eosterm.EostermThesaurusLoader;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.ThesaurusType;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * The type Thesaurus handler.
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class ThesaurusHandler {

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
        ThesaurusJobModel thesaurusJobModel = new ThesaurusJobModel();
        thesaurusJobModel.setType(thesaurusType);
        thesaurusJobModel.setStatus("PENDING");
        log.info("job:{}", thesaurusJobModel);
        return Mono.just(thesaurusJobModel);
    }

    /**
     * Run job mono.
     *
     * @param type the type
     * @return the mono
     */
    public Mono<ThesaurusJobModel> runJob(String type) {
        log.info("type:{}", type);
        ThesaurusType thesaurusType = ThesaurusType.fromString(type);
        Flux<String> load = switch (thesaurusType) {
            case ESA -> esaThesaurusLoader.load();
            case EOSTERM -> eostermThesaurusLoader.load();
            case EARTH -> earthThesaurusLoader.load();
            default -> throw new IllegalStateException("Unexpected value: " + thesaurusType);
        };
        log.info("load:{}", load);
        Mono<ThesaurusJobModel> thesaurusJobModelMono = load.collect(Collectors.counting()).map(count -> {
            ThesaurusJobModel thesaurusJobModel = new ThesaurusJobModel();
            thesaurusJobModel.setType(thesaurusType);
            thesaurusJobModel.setStatus("PENDING");
            thesaurusJobModel.setCount(count);
            return thesaurusJobModel;
        });
        log.info("job:{}", load);
        return thesaurusJobModelMono;
    }

    /**
     * Load job flux.
     *
     * @param type the type
     * @return the flux
     */
    public Flux<String> loadJob(String type) {
        log.info("type:{}", type);
        ThesaurusType thesaurusType = ThesaurusType.fromString(type);
        Flux<String> load = switch (thesaurusType) {
            case ESA -> esaThesaurusLoader.load();
            case EOSTERM -> eostermThesaurusLoader.load();
            case EARTH -> earthThesaurusLoader.load();
            default -> throw new IllegalStateException("Unexpected value: " + thesaurusType);
        };
        log.info("load:{}", load);
        return load;
    }
}
