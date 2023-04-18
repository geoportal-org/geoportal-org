package com.eversis.esa.geoss.settings.common.support;

import com.eversis.esa.geoss.settings.common.model.VersionedModel;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * The type Long to versioned model converter.
 */
@Component
public class LongToVersionedModelConverter implements Converter<Long, VersionedModel> {

    @Override
    public VersionedModel convert(Long version) {
        VersionedModel versionedModel = new VersionedModel();
        versionedModel.setVersion(version);
        return versionedModel;
    }
}
