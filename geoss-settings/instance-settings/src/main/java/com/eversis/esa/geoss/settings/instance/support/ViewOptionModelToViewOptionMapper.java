package com.eversis.esa.geoss.settings.instance.support;

import com.eversis.esa.geoss.settings.instance.domain.ViewOption;
import com.eversis.esa.geoss.settings.instance.model.ViewOptionModel;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * The interface View option model to view option mapper.
 */
@Mapper(componentModel = ComponentModel.SPRING)
public interface ViewOptionModelToViewOptionMapper {

    /**
     * Update view option.
     *
     * @param viewOptionModel the view option model
     * @param viewOption the view option
     * @return the view option
     */
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    ViewOption update(ViewOptionModel viewOptionModel, @MappingTarget ViewOption viewOption);
}
