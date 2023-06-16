package com.eversis.esa.geoss.proxy.mapper.impl;

import com.eversis.esa.geoss.proxy.document.DsParentsGroup;
import com.eversis.esa.geoss.proxy.document.DsSourcesGroup;
import com.eversis.esa.geoss.proxy.document.DsViewsGroup;
import com.eversis.esa.geoss.proxy.document.SearchResultDoc;
import com.eversis.esa.geoss.proxy.domain.LoggedSearchResultModel;
import com.eversis.esa.geoss.proxy.mapper.ElasticsearchDocumentMapper;
import org.springframework.stereotype.Component;

/**
 * The type Search result mapper.
 */
@Component
public class SearchResultMapper implements ElasticsearchDocumentMapper<LoggedSearchResultModel> {

    public SearchResultDoc mapToDocument(LoggedSearchResultModel model) {
        return getSearchResultDoc(model);
    }

    private SearchResultDoc getSearchResultDoc(LoggedSearchResultModel model) {
        if (model == null) {
            return null;
        }
        SearchResultDoc doc = new SearchResultDoc();
        doc.setUiObjectId(model.getUiObjectId());
        doc.setUiObjectClass(model.getUiObjectClass());
        doc.setSessionSiteUrl(model.getSessionSiteUrl());
        doc.setUiSource(model.getUiSource());
        doc.setUiEntryid(model.getUiEntryid());
        doc.setUiAction(model.getUiAction());
        doc.setDsQueryUrl(model.getDsQueryUrl());
        doc.setDsReqId(model.getDsReqId());
        doc.setDsSi(model.getDsSi());
        doc.setDsCt(model.getDsCt());
        doc.setDsTs(model.getDsTs());
        doc.setDsTe(model.getDsTe());
        doc.setDsSt(model.getDsSt());
        doc.setDsRel(model.getDsRel());
        doc.setDsKwd(model.getDsKwd());
        if (model.getDsParentsGroup() != null) {
            doc.setDsParentsGroupDoc(new DsParentsGroup(model.getDsParentsGroup().getKey(), model.getDsParentsGroup().getValue()));
        } else {
            doc.setDsParentsGroupDoc(new DsParentsGroup("", ""));
        }
        doc.setDsSba(model.getDsSba());
        if (model.getDsSourcesGroup() != null) {
            doc.setDsSourcesGroupDoc(new DsSourcesGroup(model.getDsSourcesGroup().getKey(), model.getDsSourcesGroup().getValue()));
        } else {
            doc.setDsSourcesGroupDoc(new DsSourcesGroup("", ""));
        }
        if (model.getDsViewsGroup() != null) {
            doc.setDsViewsGroupDoc(new DsViewsGroup(model.getDsViewsGroup().getKey(), model.getDsViewsGroup().getValue()));
        } else {
            doc.setDsViewsGroupDoc(new DsViewsGroup("", ""));
        }
        doc.setDsBbox(model.getDsBbox());
        doc.setDsGdc(model.getDsGdc());
        doc.setDsW3w(model.getDsW3w());
        doc.setDsFrmt(model.getDsFrmt());
        doc.setDsProt(model.getDsProt());
        doc.setDsScore(model.getDsScore());
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
