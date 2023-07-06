package com.eversis.esa.geoss.settings.instance.support;

import com.eversis.esa.geoss.settings.instance.domain.CatalogOption;
import com.eversis.esa.geoss.settings.instance.repository.CatalogOptionRepository;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * The type Catalog option id to catalog option converter.
 */
@Mapper(componentModel = ComponentModel.SPRING, implementationPackage = "<PACKAGE_NAME>.internal")
public abstract class CatalogOptionIdToCatalogOptionConverter implements Converter<Long, CatalogOption> {

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

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Override
    public abstract CatalogOption convert(Long source);

    /**
     * Create catalog option.
     *
     * @param id the id
     * @return the catalog option
     */
    @ObjectFactory
    protected CatalogOption create(Long id) {
        return catalogOptionRepository.getReferenceById(id);
    }
}
