package com.eversis.esa.geoss.curatedrelationships.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The type Geoss cr application.
 */
@SpringBootApplication
public class GeossCRApplication {

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
        SpringApplication.run(GeossCRApplication.class, args);
    }
}
