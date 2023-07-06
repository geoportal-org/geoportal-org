package com.eversis.esa.geoss.settings.instance.support;

import com.eversis.esa.geoss.settings.common.model.VersionedModel;
import com.eversis.esa.geoss.settings.instance.domain.Tag;
import com.eversis.esa.geoss.settings.instance.model.TagModel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.core.convert.converter.Converter;

/**
 * The interface Tag to tag model converter.
 */
@Mapper(componentModel = ComponentModel.SPRING, implementationPackage = "<PACKAGE_NAME>.internal")
public interface TagToTagModelConverter extends Converter<Tag, TagModel> {

    @Mapping(target = "versioned", source = "version")
    @Override
    TagModel convert(Tag source);

    /**
     * Map versioned model.
     *
     * @param version the version
     * @return the versioned model
     */
    default VersionedModel map(Long version) {
        if (version == null) {
            return null;
        }
        VersionedModel versionedModel = new VersionedModel();
        versionedModel.setVersion(version);
        return versionedModel;
    }
}
