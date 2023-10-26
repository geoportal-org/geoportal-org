package com.eversis.esa.geoss.common.boot;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * The type Application command line runner.
 */
@Log4j2
@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.debug("Started with args:{}", Arrays.asList(args));
        log.debug("environment:{}", System.getenv());
        log.debug("properties:{}", System.getProperties());
    }
}
