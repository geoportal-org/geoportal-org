package com.eversis.esa.geoss.settings.instance.support;

import com.eversis.esa.geoss.settings.common.model.VersionedModel;
import com.eversis.esa.geoss.settings.common.support.AuditableEmbeddableToAuditableModelConverter;
import com.eversis.esa.geoss.settings.common.support.LongToVersionedModelConverter;
import com.eversis.esa.geoss.settings.instance.domain.Layer;
import com.eversis.esa.geoss.settings.instance.model.LayerModel;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.core.convert.converter.Converter;

/**
 * The interface Layer to layer model converter.
 */
@Mapper(
        componentModel = ComponentModel.SPRING,
        implementationPackage = "<PACKAGE_NAME>.internal",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                AuditableEmbeddableToAuditableModelConverter.class,
                LongToVersionedModelConverter.class
        }
)
public interface LayerToLayerModelConverter extends Converter<Layer, LayerModel> {

    @Mapping(target = "versioned", source = "version")
    @Override
    LayerModel convert(Layer source);
}
