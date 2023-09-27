package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.EsaThesaurusLoader;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.earth.EarthThesaurusLoader;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.eosterm.EostermThesaurusLoader;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

/**
 * The type Thesaurus reader job.
 */
@Log4j2
@Configuration
public class ThesaurusReaderJob {

    private final EsaThesaurusLoader esaThesaurusLoader;
    private final EostermThesaurusLoader eostermThesaurusLoader;
    private final EarthThesaurusLoader earthThesaurusLoader;

    @Value("${thesaurus.reader.job.enabled}")
    private boolean thesaurusReaderJobEnabled;

    /**
     * Instantiates a new Thesaurus reader job.
     *
     * @param esaThesaurusLoader the esa thesaurus loader
     * @param eostermThesaurusLoader the eosterm thesaurus loader
     * @param earthThesaurusLoader the earth thesaurus loader
     */
    @Autowired
    public ThesaurusReaderJob(
            EsaThesaurusLoader esaThesaurusLoader,
            EostermThesaurusLoader eostermThesaurusLoader,
            EarthThesaurusLoader earthThesaurusLoader) {
        this.esaThesaurusLoader = esaThesaurusLoader;
        this.eostermThesaurusLoader = eostermThesaurusLoader;
        this.earthThesaurusLoader = earthThesaurusLoader;
    }

    /**
     * Command line runner command line runner.
     *
     * @param ctx the ctx
     * @return the command line runner
     */
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                if (thesaurusReaderJobEnabled) {
                    Flux.merge(
                                    esaThesaurusLoader.load(),
                                    eostermThesaurusLoader.load(),
                                    earthThesaurusLoader.load())
                            .blockLast();
                }
            }
        };
    }
}
