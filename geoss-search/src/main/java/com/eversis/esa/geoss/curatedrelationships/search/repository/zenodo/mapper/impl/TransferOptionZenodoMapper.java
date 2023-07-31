package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.model.ZenodoFile;
import com.eversis.esa.geoss.curatedrelationships.search.utils.StringUtils;

import org.springframework.stereotype.Component;

/**
 * The type Transfer option zenodo mapper.
 */
@Component
class TransferOptionZenodoMapper {

    /**
     * Map transfer option from self link transfer option.
     *
     * @param resultName the result name
     * @param simpleUrl the simple url
     * @return the transfer option
     */
    TransferOption mapTransferOptionFromSelfLink(String resultName, String simpleUrl) {
        if (StringUtils.isBlank(resultName) || StringUtils.isBlank(simpleUrl)) {
            return null;
        }

        return TransferOption.builder()
                .id(simpleUrl)
                .name("Link to : " + resultName)
                .url(simpleUrl)
                .urlType("HTTP")
                .protocol("HTTP")
                .build();
    }

    /**
     * Map transfer option from self link transfer option.
     *
     * @param zenodoFile the zenodo file
     * @return the transfer option
     */
    TransferOption mapTransferOptionFromSelfLink(ZenodoFile zenodoFile) {
        if (zenodoFile == null) {
            return null;
        }
        return TransferOption.builder()
                .id(zenodoFile.getChecksum())
                .name(zenodoFile.getKey())
                .url(zenodoFile.getFileLink())
                .urlType("HTTP")
                .protocol(zenodoFile.getType())
                .build();
    }
}
