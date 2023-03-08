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
    List<String> extensionTypes = Arrays.asList("bmp","css","doc","docx","dot","eot","flv","gif","htm","html","jpg",
            "jrxml","js","map","mp3","mp4","odb","odf","odg","odp","ods","odt","pdf","png","ppt","pptx","rtf","svg",
            "swf","sxc","sxi","sxw","tiff","ttf","txt","vsd","woff","xls","xlsx","xml");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return extensionTypes.contains(value.toLowerCase());
    }

}
