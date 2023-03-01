package com.eversis.esa.geoss.settings.instance.support;

import com.eversis.esa.geoss.settings.instance.domain.CatalogOption;
import com.eversis.esa.geoss.settings.instance.model.CatalogOptionModel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.core.convert.converter.Converter;

/**
 * The interface Catalog option to catalog option model converter.
 */
@Mapper(componentModel = ComponentModel.SPRING)
public interface CatalogOptionToCatalogOptionModelConverter extends Converter<CatalogOption, CatalogOptionModel> {

    @Mapping(target = "catalogId", source = "catalog.id")
    @Override
    CatalogOptionModel convert(CatalogOption source);
}
