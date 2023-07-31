package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.DatasetResourceCkan;

import org.springframework.stereotype.Component;

/**
 * The type Transfer option ckan mapper.
 */
@Component
class TransferOptionCkanMapper {

    /**
     * Map transfer option transfer option.
     *
     * @param resource the resource
     * @return the transfer option
     */
    TransferOption mapTransferOption(DatasetResourceCkan resource) {
        if (resource == null) {
            return null;
        }

        return TransferOption.builder()
                .id(resource.getId())
                .name(resource.getName())
                .displayTitle(resource.getDescription())
                .description(resource.getDescription())
                .url(resource.getUrl())
                .urlType("HTTP")
                .protocol(resource.getFormat())
                .build();
    }

}
