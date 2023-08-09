package com.eversis.esa.geoss.contents;

import com.eversis.esa.geoss.contents.configuration.RepositoryProperties;
import com.eversis.esa.geoss.contents.service.RepositoryService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import jakarta.annotation.Resource;

/**
 * The type Geoss contents application.
 */
@SpringBootApplication
@EnableConfigurationProperties({
        RepositoryProperties.class
})
public class GeossContentsApplication implements CommandLineRunner {

    /**
     * The Repository service.
     */
    @Resource
    RepositoryService repositoryService;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(GeossContentsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        repositoryService.init();
    }

}
