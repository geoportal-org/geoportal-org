package com.eversis.esa.geoss.settings.instance.support;

import com.eversis.esa.geoss.settings.instance.domain.View;
import com.eversis.esa.geoss.settings.instance.repository.ViewRepository;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * The type View id to view converter.
 */
@Mapper(componentModel = ComponentModel.SPRING)
public abstract class ViewIdToViewConverter implements Converter<Long, View> {

    private ViewRepository viewRepository;

    /**
     * Sets view repository.
     *
     * @param viewRepository the view repository
     */
    @Autowired
    public void setViewRepository(ViewRepository viewRepository) {
        this.viewRepository = viewRepository;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Override
    public abstract View convert(Long source);

    /**
     * Create view.
     *
     * @param id the id
     * @return the view
     */
    @ObjectFactory
    View create(Long id) {
        return viewRepository.getReferenceById(id);
    }
}
