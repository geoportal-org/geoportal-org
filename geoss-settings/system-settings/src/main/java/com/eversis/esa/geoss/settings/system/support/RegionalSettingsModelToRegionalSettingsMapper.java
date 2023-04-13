package com.eversis.esa.geoss.settings.system.support;

import com.eversis.esa.geoss.settings.system.domain.RegionalSettings;
import com.eversis.esa.geoss.settings.system.model.RegionalSettingsModel;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * The interface Regional settings model to regional settings mapper.
 */
@Mapper(componentModel = ComponentModel.SPRING)
public interface RegionalSettingsModelToRegionalSettingsMapper {

    /**
     * Update regional settings.
     *
     * @param regionalSettingsModel the regional settings model
     * @param regionalSettings the regional settings
     * @return the regional settings
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "auditable", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    RegionalSettings update(RegionalSettingsModel regionalSettingsModel,
            @MappingTarget RegionalSettings regionalSettings);
}
