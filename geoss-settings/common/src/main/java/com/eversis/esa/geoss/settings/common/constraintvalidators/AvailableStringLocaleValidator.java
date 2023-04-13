package com.eversis.esa.geoss.settings.common.constraintvalidators;

import com.eversis.esa.geoss.settings.common.constraints.AvailableLocale;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Available locale validator.
 */
public class AvailableStringLocaleValidator implements ConstraintValidator<AvailableLocale, String> {

    private static final Set<String> LOCALES = Arrays.stream(Locale.getAvailableLocales()).map(Locale::toString)
            .collect(Collectors.toSet());

    private boolean required;

    @Override
    public void initialize(AvailableLocale availableLocale) {
        this.required = availableLocale.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!required && value == null) {
            return true;
        }
        return LOCALES.contains(value);
    }
}
