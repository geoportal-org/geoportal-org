package com.eversis.esa.geoss.contents.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.validation.Validator;

import java.util.Map;
import java.util.Set;

/**
 * The type Validator registry configuration.
 */
@Configuration
public class ValidatorRegistryConfiguration implements InitializingBean {

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

    /**
     * The Validating repository event listener.
     */
    @Autowired
    ValidatingRepositoryEventListener validatingRepositoryEventListener;

    @Autowired
    private Map<String, Validator> validators;

    @Override
    public void afterPropertiesSet() throws Exception {
        for (Map.Entry<String, Validator> entry : validators.entrySet()) {
            REPOSITORY_EVENTS.stream()
                    .filter(p -> entry.getKey().startsWith(p))
                    .findFirst()
                    .ifPresent(
                            p -> validatingRepositoryEventListener
                                    .addValidator(p, entry.getValue()));
        }
    }
}
