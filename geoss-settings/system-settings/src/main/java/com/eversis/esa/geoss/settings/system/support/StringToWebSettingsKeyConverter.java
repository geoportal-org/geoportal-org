package com.eversis.esa.geoss.settings.system.support;

import com.eversis.esa.geoss.settings.system.domain.WebSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.WebSettingsSet;

import org.springframework.core.convert.converter.Converter;

/**
 * The type String to web settings key converter.
 */
public class StringToWebSettingsKeyConverter implements Converter<String, WebSettingsKey> {

    @Override
    public WebSettingsKey convert(String source) {
        if (source.isEmpty()) {
            // It's an empty enum identifier: reset the enum value to null.
            return null;
        }
        return WebSettingsSet.keyFromString(source);
    }
}
