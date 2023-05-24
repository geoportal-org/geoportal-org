package com.eversis.esa.geoss.settings.common.constraintvalidators;

import com.eversis.esa.geoss.settings.common.constraints.URI;
import com.eversis.esa.geoss.settings.common.constraints.URI.Type;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * The type Uri validator.
 */
public class URIValidator implements ConstraintValidator<URI, String> {

    private static final String PROTOCOL_RELATIVE_PREFIX = "//";

    private static final String RELATIVE_PREFIX = "/";

    private Type type;

    @Override
    public void initialize(URI uri) {
        this.type = uri.type();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            java.net.URI uri = java.net.URI.create(value);
            return switch (type) {
                case AUTO -> true;
                case ABSOLUTE -> uri.isAbsolute();
                case OPAQUE -> uri.isOpaque();
                case PROTOCOL_RELATIVE -> uri.toString().startsWith(PROTOCOL_RELATIVE_PREFIX);
                case RELATIVE -> !uri.isAbsolute() && !uri.isOpaque() && uri.toString().startsWith(RELATIVE_PREFIX);
                default -> throw new IllegalArgumentException("Unexpected uri type: " + type);
            };
        } catch (RuntimeException e) {
            return false;
        }
    }
}
