package com.eversis.esa.geoss.personaldata.common.constraints;

import com.eversis.esa.geoss.personaldata.common.constraintvalidators.URIValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Uri.
 */
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = URIValidator.class)
@Documented
public @interface URI {

    /**
     * Message string.
     *
     * @return the string
     */
    String message() default "{com.eversis.validation.constraints.URI.message}";

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
     * Absolute boolean.
     *
     * @return the boolean
     */
    boolean absolute() default true;
}
