package com.eversis.esa.geoss.proxy.mapper.impl;

import com.eversis.esa.geoss.proxy.document.ElementClickDoc;
import com.eversis.esa.geoss.proxy.domain.LoggedElementClickModel;
import com.eversis.esa.geoss.proxy.mapper.ElasticsearchDocumentMapper;
import org.springframework.stereotype.Component;

/**
 * The type Element click mapper.
 */
@Component
public class ElementClickMapper implements ElasticsearchDocumentMapper<LoggedElementClickModel> {

    @Override
    public ElementClickDoc mapToDocument(LoggedElementClickModel model) {
        return getElementClickDoc(model);
    }

    private ElementClickDoc getElementClickDoc(LoggedElementClickModel model) {
        if (model == null) {
            return null;
        }
        ElementClickDoc doc = new ElementClickDoc();
        doc.setUiObjectId(model.getUiObjectId());
        doc.setUiObjectClass(model.getUiObjectClass());
        doc.setUiSource(model.getUiSource());
        doc.setSessionSiteUrl(model.getSessionSiteUrl());
        doc.setUiEntryid(model.getUiEntryId());
        doc.setUiAction(model.getUiAction());
        doc.setUiLabel(model.getUiLabel());
        doc.setUiOrganisation(model.getUiOrganisation());
        doc.setUiResourceName(model.getUiResourceName());
        doc.setOperation(model.getOperation());
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
