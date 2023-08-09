package com.eversis.esa.geoss.proxy.mapper.impl;

import com.eversis.esa.geoss.proxy.document.SignInDoc;
import com.eversis.esa.geoss.proxy.domain.LoggedSignInModel;
import com.eversis.esa.geoss.proxy.mapper.ElasticsearchDocumentMapper;

import org.springframework.stereotype.Component;

/**
 * The type Sign in mapper.
 */
@Component
public class SignInMapper implements ElasticsearchDocumentMapper<LoggedSignInModel> {

    private static final String RESULT = "Successfully logged in";

    private static final String OPERATION = "Login attempt";

    @Override
    public SignInDoc mapToDocument(LoggedSignInModel model) {
        return getSignInDoc(model);
    }

    private SignInDoc getSignInDoc(LoggedSignInModel model) {
        if (model == null) {
            return null;
        }
        SignInDoc doc = new SignInDoc();
        doc.setResult(RESULT);
        doc.setOperation(OPERATION);
        doc.setSessionSiteUrl(model.getSessionSiteUrl());
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
