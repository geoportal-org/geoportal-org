package com.eversis.esa.geoss.proxy.mapper.impl;

import com.eversis.esa.geoss.proxy.document.ViewCounterDoc;
import com.eversis.esa.geoss.proxy.domain.ViewCounterModel;
import com.eversis.esa.geoss.proxy.mapper.ElasticsearchDocumentMapper;

import org.springframework.stereotype.Component;

/**
 * The type View counter mapper.
 */
@Component
public class ViewCounterMapper implements ElasticsearchDocumentMapper<ViewCounterModel> {

    @Override
    public ViewCounterDoc mapToDocument(ViewCounterModel model) {
        if (model == null) {
            return null;
        }
        ViewCounterDoc doc = new ViewCounterDoc();
        doc.setUiObjectId(model.getUiObjectId());
        doc.setUiObjectClass(model.getUiObjectClass());
        doc.setUiSource(model.getUiSource());
        doc.setSessionSiteUrl(model.getSessionSiteUrl());
        doc.setUiEntryid(model.getUiEntryId());
        doc.setUiAction(model.getUiAction());
        doc.setUiLabel(model.getUiLabel());
        doc.setUiOrganisation(model.getUiOrganisation());
        doc.setUiResourceName(model.getUiResourceName());
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
        doc.setWinWidth(model.getCommonProperties().getOtherProperties().getWinWidth());
        doc.setWinHeight(model.getCommonProperties().getOtherProperties().getWinHeight());
        return doc;
    }

}
