package com.eversis.esa.geoss.settings.application.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.validation.Validator;

import java.util.Set;

/**
 * The type Data rest configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class DataRestConfiguration implements InitializingBean {

    private static final Set<String> REPOSITORY_EVENTS = Set.of(
            "beforeCreate",
            "afterCreate",
            "beforeSave",
            "afterSave",
            "beforeLinkSave",
            "afterLinkSave",
            "beforeLinkDelete",
            "afterLinkDelete",
            "beforeDelete",
            "afterDelete"
    );

    private final Validator validator;

    private final ValidatingRepositoryEventListener validatingRepositoryEventListener;

    @Override
    public void afterPropertiesSet() throws Exception {
        REPOSITORY_EVENTS.forEach(event -> validatingRepositoryEventListener.addValidator(event, validator));
    }
}
