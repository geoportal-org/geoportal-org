package com.eversis.esa.geoss.settings.system.support;

import com.eversis.esa.geoss.settings.system.domain.RegionalSettingsKey;

import org.springframework.core.convert.converter.Converter;

/**
 * The type String to regional settings key converter.
 */
public class StringToRegionalSettingsKeyConverter implements Converter<String, RegionalSettingsKey> {

    @Override
    public RegionalSettingsKey convert(String source) {
        if (source.isEmpty()) {
            // It's an empty enum identifier: reset the enum value to null.
            return null;
        }
        return RegionalSettingsKey.fromString(source.trim());
    }
}
