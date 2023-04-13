package com.eversis.esa.geoss.settings.common.constraints;

import com.eversis.esa.geoss.settings.common.constraintvalidators.AvailableStringTimeZoneValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Available time zone.
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AvailableStringTimeZoneValidator.class)
@Documented
public @interface AvailableTimeZone {

    /**
     * Message string.
     *
     * @return the string
     */
    String message() default "{com.eversis.esa.geoss.settings.common.constraints.AvailableTimeZone.message}";

    /**
     * Groups class [ ].
     *
     * @return the class [ ]
     */
    Class<?>[] groups() default {};

    /**
     * Payload class [ ].
     *
     * @return the class [ ]
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Required boolean.
     *
     * @return the boolean
     */
    boolean required() default true;
}
