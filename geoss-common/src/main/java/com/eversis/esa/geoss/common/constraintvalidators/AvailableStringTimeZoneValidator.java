package com.eversis.esa.geoss.common.constraintvalidators;

import com.eversis.esa.geoss.common.constraints.AvailableTimeZone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * The type Available locale validator.
 */
public class AvailableStringTimeZoneValidator implements ConstraintValidator<AvailableTimeZone, String> {

    private static final Set<String> TIME_ZONES = Arrays.stream(TimeZone.getAvailableIDs()).collect(Collectors.toSet());

    private boolean required;

    @Override
    public void initialize(AvailableTimeZone availableLocale) {
        this.required = availableLocale.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!required && value == null) {
            return true;
        }
        return TIME_ZONES.contains(value);
    }
}
