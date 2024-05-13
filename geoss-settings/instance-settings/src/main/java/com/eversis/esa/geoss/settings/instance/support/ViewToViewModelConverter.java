package com.eversis.esa.geoss.settings.instance.support;

import com.eversis.esa.geoss.settings.common.support.AuditableEmbeddableToAuditableModelConverter;
import com.eversis.esa.geoss.settings.common.support.LongToVersionedModelConverter;
import com.eversis.esa.geoss.settings.instance.domain.View;
import com.eversis.esa.geoss.settings.instance.model.ViewModel;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.core.convert.converter.Converter;

/**
 * The interface View to view model converter.
 */
@Mapper(
        componentModel = ComponentModel.SPRING,
        implementationPackage = "<PACKAGE_NAME>.internal",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                AuditableEmbeddableToAuditableModelConverter.class,
                LongToVersionedModelConverter.class,
                ViewOptionToViewOptionModelConverter.class
        }
)
public interface ViewToViewModelConverter extends Converter<View, ViewModel> {

    @Mapping(target = "versioned", source = "version")
    @Override
    ViewModel convert(View source);
}
