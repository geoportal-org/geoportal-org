package com.eversis.esa.geoss.settings.common.constraints;

import com.eversis.esa.geoss.settings.common.constraintvalidators.URIValidator;

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
    String message() default "{com.eversis.validation.common.constraints.URI.message}";

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
     * Access type.
     *
     * @return the type
     */
    Type type() default Type.AUTO;

    /**
     * The enum Type.
     */
    enum Type {
        /**
         * Auto type.
         */
        AUTO,
        /**
         * Absolute type.
         */
        ABSOLUTE,

        /**
         * Opaque type.
         */
        OPAQUE,

        /**
         * Protocol relative type.
         */
        PROTOCOL_RELATIVE,

        /**
         * Relative type.
         */
        RELATIVE
    }
}
