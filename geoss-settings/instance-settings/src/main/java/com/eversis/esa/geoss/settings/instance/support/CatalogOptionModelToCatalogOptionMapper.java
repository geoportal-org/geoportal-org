package com.eversis.esa.geoss.settings.instance.support;

import com.eversis.esa.geoss.settings.instance.domain.CatalogOption;
import com.eversis.esa.geoss.settings.instance.model.CatalogOptionModel;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * The interface Catalog option model to catalog option mapper.
 */
@Mapper(componentModel = ComponentModel.SPRING)
public interface CatalogOptionModelToCatalogOptionMapper {

    /**
     * Update catalog option.
     *
     * @param catalogOptionModel the catalog option model
     * @param catalogOption the catalog option
     * @return the catalog option
     */
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    CatalogOption update(CatalogOptionModel catalogOptionModel, @MappingTarget CatalogOption catalogOption);
}
