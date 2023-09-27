package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.webflux;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.EsaThesaurusLoader;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.earth.EarthThesaurusLoader;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.eosterm.EostermThesaurusLoader;

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
     * Gets esa thesaurus.
     *
     * @return the esa thesaurus
     */
    public Mono<ThesaurusJobModel> getEsaThesaurus() {
        ThesaurusJobModel thesaurusJobModel = new ThesaurusJobModel();
        thesaurusJobModel.setName("esa");
        thesaurusJobModel.setStatus("PENDING");
        log.info("job:{}", thesaurusJobModel);
        return Mono.just(thesaurusJobModel);
    }

    /**
     * Load esa thesaurus mono.
     *
     * @return the mono
     */
    public Mono<ThesaurusJobModel> loadEsaThesaurus() {
        log.info("job:{}", "esa");
        Mono<ThesaurusJobModel> load = esaThesaurusLoader.load().collect(Collectors.counting()).map(count -> {
            ThesaurusJobModel thesaurusJobModel = new ThesaurusJobModel();
            thesaurusJobModel.setName("esa");
            thesaurusJobModel.setStatus("PENDING");
            thesaurusJobModel.setCount(count);
            return thesaurusJobModel;
        });
        log.info("job:{}", load);
        return load;
    }

    /**
     * Gets eosterm thesaurus.
     *
     * @return the eosterm thesaurus
     */
    public Mono<ThesaurusJobModel> getEostermThesaurus() {
        ThesaurusJobModel thesaurusJobModel = new ThesaurusJobModel();
        thesaurusJobModel.setName("eosterm");
        thesaurusJobModel.setStatus("PENDING");
        log.info("job:{}", thesaurusJobModel);
        return Mono.just(thesaurusJobModel);
    }

    /**
     * Load eosterm thesaurus mono.
     *
     * @return the mono
     */
    public Mono<ThesaurusJobModel> loadEostermThesaurus() {
        log.info("job:{}", "eosterm");
        Mono<ThesaurusJobModel> load = eostermThesaurusLoader.load().collect(Collectors.counting()).map(count -> {
            ThesaurusJobModel thesaurusJobModel = new ThesaurusJobModel();
            thesaurusJobModel.setName("eosterm");
            thesaurusJobModel.setStatus("PENDING");
            thesaurusJobModel.setCount(count);
            return thesaurusJobModel;
        });
        log.info("job:{}", load);
        return load;
    }

    /**
     * Gets earth thesaurus.
     *
     * @return the earth thesaurus
     */
    public Mono<ThesaurusJobModel> getEarthThesaurus() {
        ThesaurusJobModel thesaurusJobModel = new ThesaurusJobModel();
        thesaurusJobModel.setName("earth");
        thesaurusJobModel.setStatus("PENDING");
        log.info("job:{}", thesaurusJobModel);
        return Mono.just(thesaurusJobModel);
    }

    /**
     * Load earth thesaurus mono.
     *
     * @return the mono
     */
    public Mono<ThesaurusJobModel> loadEarthThesaurus() {
        log.info("job:{}", "earth");
        Mono<ThesaurusJobModel> load = earthThesaurusLoader.load().collect(Collectors.counting()).map(count -> {
            ThesaurusJobModel thesaurusJobModel = new ThesaurusJobModel();
            thesaurusJobModel.setName("earth");
            thesaurusJobModel.setStatus("PENDING");
            thesaurusJobModel.setCount(count);
            return thesaurusJobModel;
        });
        log.info("job:{}", load);
        return load;
    }

    /**
     * Gets all.
     *
     * @return the all
     */
    public Mono<ThesaurusJobModel> getAll() {
        ThesaurusJobModel thesaurusJobModel = new ThesaurusJobModel();
        thesaurusJobModel.setName("all");
        thesaurusJobModel.setStatus("PENDING");
        log.info("job:{}", thesaurusJobModel);
        return Mono.just(thesaurusJobModel);
    }

    /**
     * Load all mono.
     *
     * @return the mono
     */
    public Mono<ThesaurusJobModel> loadAll() {
        log.info("job:{}", "load");
        Mono<ThesaurusJobModel> merge = Flux
                .merge(
                        esaThesaurusLoader.load(),
                        eostermThesaurusLoader.load(),
                        earthThesaurusLoader.load())
                .collect(Collectors.counting())
                .map(count -> {
                    ThesaurusJobModel thesaurusJobModel = new ThesaurusJobModel();
                    thesaurusJobModel.setName("all");
                    thesaurusJobModel.setStatus("PENDING");
                    thesaurusJobModel.setCount(count);
                    return thesaurusJobModel;
                });
        log.info("job:{}", merge);
        return merge;
    }
}
