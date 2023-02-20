package com.eversis.esa.geoss.settings.common.constraints;

import com.eversis.esa.geoss.settings.common.constraintvalidators.AvailableLocaleValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Available locale.
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AvailableLocaleValidator.class)
@Documented
public @interface AvailableLocale {

    /**
     * Message string.
     *
     * @return the string
     */
    String message() default "{com.eversis.esa.geoss.settings.common.constraints.AvailableLocale.message}";

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
