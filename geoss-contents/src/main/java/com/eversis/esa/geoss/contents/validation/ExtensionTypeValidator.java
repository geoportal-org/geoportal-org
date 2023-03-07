package com.eversis.esa.geoss.contents.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

/**
 * The type Extension type validator.
 */
public class ExtensionTypeValidator implements ConstraintValidator<ExtensionType, String> {

    /**
     * The Extension types.
     */
    List<String> extensionTypes = Arrays.asList("pdf", "doc", "docx", "png", "jpg");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return extensionTypes.contains(value.toLowerCase());
    }

}
