package com.eversis.esa.geoss.personaldata.application.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.hateoas.mediatype.hal.HalConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * The type Hateoas configuration.
 */
@Log4j2
@RequiredArgsConstructor
@EnableHypermediaSupport(
        type = {HypermediaType.HAL, HypermediaType.HAL_FORMS, HypermediaType.COLLECTION_JSON, HypermediaType.UBER})
@Configuration(proxyBeanMethods = false)
public class HateoasConfiguration {

    /**
     * Expose ids repository rest configurer repository rest configurer.
     *
     * @param repositories the repositories
     * @return the repository rest configurer
     */
    @Bean
    RepositoryRestConfigurer exposeIdsRepositoryRestConfigurer(final Repositories repositories) {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration repositoryRestConfiguration,
                    CorsRegistry cors) {
                repositories.forEach(repositoryRestConfiguration::exposeIdsFor);
            }
        };
    }

    @Primary
    @Bean
    HalConfiguration halConfiguration() {
        HalConfiguration halConfiguration = new HalConfiguration()
                .withMediaType(MediaType.APPLICATION_JSON)
                .withMediaType(MediaTypes.HAL_JSON)
                .withMediaType(MediaTypes.HAL_FORMS_JSON)
                .withMediaType(MediaTypes.COLLECTION_JSON)
                .withMediaType(MediaTypes.UBER_JSON);
        return halConfiguration;
    }
}
