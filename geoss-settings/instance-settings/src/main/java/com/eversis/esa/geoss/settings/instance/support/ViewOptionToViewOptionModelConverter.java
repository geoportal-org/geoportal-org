package com.eversis.esa.geoss.settings.instance.support;

import com.eversis.esa.geoss.settings.instance.domain.ViewOption;
import com.eversis.esa.geoss.settings.instance.model.ViewOptionModel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.core.convert.converter.Converter;

/**
 * The interface View option to view option model converter.
 */
@Mapper(componentModel = ComponentModel.SPRING, implementationPackage = "<PACKAGE_NAME>.internal")
public interface ViewOptionToViewOptionModelConverter extends Converter<ViewOption, ViewOptionModel> {

    @Mapping(target = "viewId", source = "view.id")
    @Override
    ViewOptionModel convert(ViewOption source);
}
