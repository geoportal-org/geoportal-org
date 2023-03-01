package com.eversis.esa.geoss.settings.instance.support;

import com.eversis.esa.geoss.settings.instance.domain.ViewOption;
import com.eversis.esa.geoss.settings.instance.model.ViewOptionModel;
import com.eversis.esa.geoss.settings.instance.repository.ViewOptionRepository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * The type View option model to view option converter.
 */
@Mapper(componentModel = ComponentModel.SPRING, uses = ViewIdToViewConverter.class)
public abstract class ViewOptionModelToViewOptionConverter implements Converter<ViewOptionModel, ViewOption> {

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

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "view", source = "viewId")
    @Override
    public abstract ViewOption convert(ViewOptionModel source);

    /**
     * Create view option view option.
     *
     * @param viewOptionModel the view option model
     * @return the view option
     */
    @ObjectFactory
    ViewOption createViewOption(ViewOptionModel viewOptionModel) {
        if (viewOptionModel.getId() == null) {
            return new ViewOption();
        }
        return viewOptionRepository.getReferenceById(viewOptionModel.getId());
    }
}
