package com.eversis.esa.geoss.settings.instance.support;

import com.eversis.esa.geoss.settings.instance.domain.ViewOption;
import com.eversis.esa.geoss.settings.instance.repository.ViewOptionRepository;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * The type View option id to view option converter.
 */
@Mapper(componentModel = ComponentModel.SPRING)
public abstract class ViewOptionIdToViewOptionConverter implements Converter<Long, ViewOption> {

    private ViewOptionRepository viewOptionRepository;

    /**
     * Sets view option repository.
     *
     * @param viewOptionRepository the view option repository
     */
    @Autowired
    public void setViewOptionRepository(ViewOptionRepository viewOptionRepository) {
        this.viewOptionRepository = viewOptionRepository;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Override
    public abstract ViewOption convert(Long source);

    /**
     * Create view option.
     *
     * @param id the id
     * @return the view option
     */
    @ObjectFactory
    ViewOption create(Long id) {
        return viewOptionRepository.getReferenceById(id);
    }
}
