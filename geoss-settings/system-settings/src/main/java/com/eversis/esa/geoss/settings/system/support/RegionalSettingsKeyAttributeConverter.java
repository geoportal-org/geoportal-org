package com.eversis.esa.geoss.settings.system.support;

import com.eversis.esa.geoss.settings.system.domain.RegionalSettingsKey;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * The type Regional settings key attribute converter.
 */
@Converter(autoApply = true)
public class RegionalSettingsKeyAttributeConverter implements AttributeConverter<RegionalSettingsKey, Boolean> {

    @Override
    public Boolean convertToDatabaseColumn(RegionalSettingsKey attribute) {
        if (attribute == null) {
            return null;
        }
        return RegionalSettingsKey.CURRENT.equals(attribute);
    }

    @Override
    public RegionalSettingsKey convertToEntityAttribute(Boolean dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData ? RegionalSettingsKey.CURRENT : null;
    }
}
