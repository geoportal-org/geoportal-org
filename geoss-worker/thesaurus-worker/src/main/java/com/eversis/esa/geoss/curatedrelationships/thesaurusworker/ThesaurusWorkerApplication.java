package com.eversis.esa.geoss.curatedrelationships.thesaurusworker;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.config.EsaThesaurusProperties;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.earth.config.EarthThesaurusProperties;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.eosterm.config.EostermThesaurusProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * The type Thesaurus worker application.
 */
@EnableConfigurationProperties(
        value = {
                EsaThesaurusProperties.class,
                EarthThesaurusProperties.class,
                EostermThesaurusProperties.class,
        }
)
@SpringBootApplication
public class ThesaurusWorkerApplication {

    static {
        // if you want to use log4j logger in this class then set this property from command line
        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.selector.BasicContextSelector");
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ThesaurusWorkerApplication.class, args);
    }
}
