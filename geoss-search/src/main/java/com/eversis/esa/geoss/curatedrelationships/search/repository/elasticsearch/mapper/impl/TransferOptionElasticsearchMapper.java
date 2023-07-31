package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model.TransferOptionELK;

import org.springframework.stereotype.Component;

/**
 * The type Transfer option elasticsearch mapper.
 */
@Component
public class TransferOptionElasticsearchMapper {

    /**
     * Map transfer option transfer option.
     *
     * @param transferOptionELK the transfer option elk
     * @return the transfer option
     */
    TransferOption mapTransferOption(TransferOptionELK transferOptionELK) {
        if (transferOptionELK == null) {
            return null;
        }

        return TransferOption.builder()
                .id(transferOptionELK.getId())
                .name(transferOptionELK.getName())
                .displayTitle(transferOptionELK.getTitle())
                .description(transferOptionELK.getDescription())
                .url(transferOptionELK.getUrl())
                .urlType(transferOptionELK.getUrlType())
                .protocol(transferOptionELK.getProtocol())
                .build();
    }

}
