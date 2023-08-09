package com.eversis.esa.geoss.proxy.mapper.impl;

import com.eversis.esa.geoss.proxy.document.ResourceErrorDoc;
import com.eversis.esa.geoss.proxy.domain.LoggedResourceErrorModel;
import com.eversis.esa.geoss.proxy.mapper.ElasticsearchDocumentMapper;

import org.springframework.stereotype.Component;

/**
 * The type Resource error mapper.
 */
@Component
public class ResourceErrorMapper implements ElasticsearchDocumentMapper<LoggedResourceErrorModel> {

    @Override
    public ResourceErrorDoc mapToDocument(LoggedResourceErrorModel model) {
        return getResourceErrorDoc(model);
    }

    private ResourceErrorDoc getResourceErrorDoc(LoggedResourceErrorModel model) {
        if (model == null) {
            return null;
        }
        ResourceErrorDoc doc = new ResourceErrorDoc();
        doc.setUiEntryId(model.getUiEntryId());
        doc.setResult(model.getResult());
        doc.setOperation(model.getOperation());
        doc.setMessage(model.getMessage());
        doc.setResultDetails(model.getResultDetails());
        doc.setSessionSiteUrl(model.getSessionSiteUrl());
        doc.setShortUrl(model.getShortUrl());
        doc.setSessionLogin(model.getCommonProperties().getSessionProperties().getSessionLogin());
        doc.setSessionId(model.getCommonProperties().getSessionProperties().getSessionId());
        doc.setSessionTimestamp(model.getCommonProperties().getSessionProperties().getSessionTimestamp());
        doc.setSessionDate(model.getCommonProperties().getSessionProperties().getSessionDate());
        doc.setUa(model.getCommonProperties().getUserAgentProperties().getUa());
        doc.setUaBrowserName(model.getCommonProperties().getUserAgentProperties().getUaBrowserName());
        doc.setUaBrowserVersion(model.getCommonProperties().getUserAgentProperties().getUaBrowserVersion());
        doc.setUaEngineName(model.getCommonProperties().getUserAgentProperties().getUaEngineName());
        doc.setUaEngineVersion(model.getCommonProperties().getUserAgentProperties().getUaEngineVersion());
        doc.setUaOsName(model.getCommonProperties().getUserAgentProperties().getUaOsName());
        doc.setUaOsVersion(model.getCommonProperties().getUserAgentProperties().getUaOsVersion());
        doc.setUaDeviceModel(model.getCommonProperties().getUserAgentProperties().getUaDeviceModel());
        doc.setUaDeviceVendor(model.getCommonProperties().getUserAgentProperties().getUaDeviceVendor());
        doc.setUaDeviceType(model.getCommonProperties().getUserAgentProperties().getUaDeviceType());
        doc.setUaCpuArchitecture(model.getCommonProperties().getUserAgentProperties().getUaCpuArchitecture());
        doc.setWinWidth(model.getCommonProperties().getOtherProperties().getWinWidth());
        doc.setWinHeight(model.getCommonProperties().getOtherProperties().getWinHeight());
        return doc;
    }
}
