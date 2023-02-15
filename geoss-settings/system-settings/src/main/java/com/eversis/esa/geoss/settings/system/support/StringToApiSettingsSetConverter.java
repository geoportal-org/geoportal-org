package com.eversis.esa.geoss.settings.system.support;

import com.eversis.esa.geoss.settings.system.domain.ApiSettingsSet;

import org.springframework.core.convert.converter.Converter;

/**
 * The type String to api settings set converter.
 */
public class StringToApiSettingsSetConverter implements Converter<String, ApiSettingsSet> {

    @Override
    public ApiSettingsSet convert(String source) {
        if (source.isEmpty()) {
            // It's an empty enum identifier: reset the enum value to null.
            return null;
        }
        return ApiSettingsSet.fromString(source.trim());
    }
}
