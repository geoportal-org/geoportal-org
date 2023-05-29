package com.eversis.esa.geoss.contents.validation;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The type Available locale validator.
 */
public class AvailableLocaleValidator implements ConstraintValidator<AvailableLocale, Locale> {

    private static final Set<Locale> LOCALES = Arrays.stream(Locale.getAvailableLocales()).collect(Collectors.toSet());

    private boolean required;

    @Override
    public void initialize(AvailableLocale availableLocale) {
        this.required = availableLocale.required();
    }

    @Override
    public boolean isValid(Locale value, ConstraintValidatorContext context) {
        if (!required && value == null) {
            return true;
        }
        return LOCALES.contains(value);
    }

}
