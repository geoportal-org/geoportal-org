package com.eversis.esa.geoss.settings.system.support;

import com.eversis.esa.geoss.settings.system.domain.ApiSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsSet;

import org.springframework.core.convert.converter.Converter;

/**
 * The type String to api settings key converter.
 */
public class StringToApiSettingsKeyConverter implements Converter<String, ApiSettingsKey> {

    @Override
    public ApiSettingsKey convert(String source) {
        if (source.isEmpty()) {
            // It's an empty enum identifier: reset the enum value to null.
            return null;
        }
        return ApiSettingsSet.keyFromString(source);
    }
}
