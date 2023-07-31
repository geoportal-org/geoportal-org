package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.DatasetResourceCkan;

import org.springframework.stereotype.Component;

@Component
class TransferOptionCkanMapper {

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
