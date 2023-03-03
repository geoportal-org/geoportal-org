package com.eversis.esa.geoss.settings.system.support;

import com.eversis.esa.geoss.settings.system.domain.ApiSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsSet;

import jakarta.persistence.AttributeConverter;

/**
 * The type Api settings key attribute converter.
 */
public class ApiSettingsKeyAttributeConverter implements AttributeConverter<ApiSettingsKey, String> {

    @Override
    public String convertToDatabaseColumn(ApiSettingsKey attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.key();
    }

    @Override
    public ApiSettingsKey convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return ApiSettingsSet.keyFromString(dbData);
    }
}
