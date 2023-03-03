package com.eversis.esa.geoss.settings.instance.support;

import com.eversis.esa.geoss.settings.instance.domain.CatalogOption;
import com.eversis.esa.geoss.settings.instance.model.CatalogOptionModel;
import com.eversis.esa.geoss.settings.instance.repository.CatalogOptionRepository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * The type Catalog option model to catalog option converter.
 */
@Mapper(componentModel = ComponentModel.SPRING, uses = CatalogIdToCatalogConverter.class)
public abstract class CatalogOptionModelToCatalogOptionConverter implements
        Converter<CatalogOptionModel, CatalogOption> {

    private CatalogOptionRepository catalogOptionRepository;

    /**
     * Sets catalog option repository.
     *
     * @param catalogOptionRepository the catalog option repository
     */
    @Autowired
    public void setCatalogOptionRepository(CatalogOptionRepository catalogOptionRepository) {
        this.catalogOptionRepository = catalogOptionRepository;
    }

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "catalog", source = "catalogId")
    @Override
    public abstract CatalogOption convert(CatalogOptionModel source);

    /**
     * Create catalog option catalog option.
     *
     * @param catalogOptionModel the catalog option model
     * @return the catalog option
     */
    @ObjectFactory
    CatalogOption createCatalogOption(CatalogOptionModel catalogOptionModel) {
        if (catalogOptionModel.getId() == null) {
            return new CatalogOption();
        }
        return catalogOptionRepository.getReferenceById(catalogOptionModel.getId());
    }
}
