package com.eversis.esa.geoss.settings.system.support;

import com.eversis.esa.geoss.settings.system.domain.RegionalSettings;
import com.eversis.esa.geoss.settings.system.model.RegionalSettingsModel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.core.convert.converter.Converter;

/**
 * The interface Regional settings to regional settings model converter.
 */
@Mapper(componentModel = ComponentModel.SPRING)
public interface RegionalSettingsToRegionalSettingsModelConverter extends
        Converter<RegionalSettings, RegionalSettingsModel> {

    @Mapping(target = "versioned.version", source = "version")
    @Override
    RegionalSettingsModel convert(RegionalSettings regionalSettings);
}
