package com.eversis.esa.geoss.common.constraintvalidators;

import com.eversis.esa.geoss.common.constraints.AvailableLocale;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

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
