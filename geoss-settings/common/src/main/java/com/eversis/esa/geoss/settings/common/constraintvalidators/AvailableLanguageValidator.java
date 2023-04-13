package com.eversis.esa.geoss.settings.common.constraintvalidators;

import com.eversis.esa.geoss.settings.common.constraints.AvailableLanguage;
import com.eversis.esa.geoss.settings.common.properties.GeossProperties;

import org.springframework.beans.factory.annotation.Configurable;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Available language validator.
 */
@Configurable
public class AvailableLanguageValidator implements ConstraintValidator<AvailableLanguage, String> {

    private boolean required;

    @Override
    public void initialize(AvailableLanguage availableLanguage) {
        this.required = availableLanguage.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!required && value == null) {
            return true;
        }
        GeossProperties geossProperties = ConstraintValidatorBeanFactoryProxy.getBean(GeossProperties.class);
        Set<String> languages = Arrays.stream(geossProperties.getSupportedLanguages())
                .collect(Collectors.toSet());
        return languages.contains(value);
    }
}
