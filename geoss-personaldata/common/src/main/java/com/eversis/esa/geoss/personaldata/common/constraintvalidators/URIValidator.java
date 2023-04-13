package com.eversis.esa.geoss.personaldata.common.constraintvalidators;

import com.eversis.esa.geoss.personaldata.common.constraints.URI;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The type Uri validator.
 */
public class URIValidator implements ConstraintValidator<URI, String> {

    private boolean absolute;

    @Override
    public void initialize(URI uri) {
        this.absolute = uri.absolute();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            java.net.URI uri = java.net.URI.create(value);
            return absolute == uri.isAbsolute();
        } catch (RuntimeException e) {
            return false;
        }
    }
}
