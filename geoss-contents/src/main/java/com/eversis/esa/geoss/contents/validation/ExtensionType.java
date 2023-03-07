package com.eversis.esa.geoss.contents.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The interface Extension type.
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ExtensionTypeValidator.class)
@Documented
public @interface ExtensionType {

    /**
     * Message string.
     *
     * @return the string
     */
    String message() default "{validation.fileExtensionNotAllowed}";

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

}
