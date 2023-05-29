package com.eversis.esa.geoss.contents.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

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
    String message() default "{validation.AvailableLocale}";

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
