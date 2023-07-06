package com.eversis.esa.geoss.settings.instance.support;

import com.eversis.esa.geoss.settings.instance.domain.Catalog;
import com.eversis.esa.geoss.settings.instance.repository.CatalogRepository;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * The type Catalog id to catalog converter.
 */
@Mapper(componentModel = ComponentModel.SPRING, implementationPackage = "<PACKAGE_NAME>.internal")
public abstract class CatalogIdToCatalogConverter implements Converter<Long, Catalog> {

    private CatalogRepository catalogRepository;

    /**
     * Sets catalog repository.
     *
     * @param catalogRepository the catalog repository
     */
    @Autowired
    public void setCatalogRepository(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Override
    public abstract Catalog convert(Long source);

    /**
     * Create catalog.
     *
     * @param id the id
     * @return the catalog
     */
    @ObjectFactory
    protected Catalog create(Long id) {
        return catalogRepository.getReferenceById(id);
    }
}
