package com.eversis.esa.geoss.settings.system.support;

import com.eversis.esa.geoss.settings.system.domain.WebSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.WebSettingsSet;

import jakarta.persistence.AttributeConverter;

/**
 * The type Web settings key attribute converter.
 */
public class WebSettingsKeyAttributeConverter implements AttributeConverter<WebSettingsKey, String> {

    @Override
    public String convertToDatabaseColumn(WebSettingsKey attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.key();
    }

    @Override
    public WebSettingsKey convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return WebSettingsSet.keyFromString(dbData);
    }
}
