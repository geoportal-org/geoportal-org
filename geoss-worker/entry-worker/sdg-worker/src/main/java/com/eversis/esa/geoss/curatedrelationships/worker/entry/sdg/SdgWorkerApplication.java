package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.EnableCoreDataModule;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.EnableCoreDataGarbageCollectorModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The type Sdg worker application.
 */
@SpringBootApplication
@EnableCoreDataModule
@EnableCoreDataGarbageCollectorModule
@EnableTransactionManagement
public class SdgWorkerApplication {

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
        SpringApplication.run(SdgWorkerApplication.class, args);
    }
}
