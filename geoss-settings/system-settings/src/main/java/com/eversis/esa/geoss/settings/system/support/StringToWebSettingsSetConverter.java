package com.eversis.esa.geoss.settings.system.support;

import com.eversis.esa.geoss.settings.system.domain.WebSettingsSet;

import org.springframework.core.convert.converter.Converter;

/**
 * The type String to web settings set converter.
 */
public class StringToWebSettingsSetConverter implements Converter<String, WebSettingsSet> {

    @Override
    public WebSettingsSet convert(String source) {
        if (source.isEmpty()) {
            // It's an empty enum identifier: reset the enum value to null.
            return null;
        }
        return WebSettingsSet.fromString(source.trim());
    }
}
