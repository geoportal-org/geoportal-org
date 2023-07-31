package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.model.OnlineResource;

public class OnlineResourceFactory {

    private OnlineResourceFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static OnlineResource createOnlineResource(TransferOption transferOption) {
        OnlineResource onlineResource = new OnlineResource();
        onlineResource.setName(transferOption.getName());
        onlineResource.setDescription(transferOption.getDisplayTitle());
        onlineResource.setDescriptionURL(transferOption.getUrlType());
        onlineResource.setProtocol(transferOption.getProtocol());
        onlineResource.setUrl(transferOption.getUrl());
        return onlineResource;
    }

}
