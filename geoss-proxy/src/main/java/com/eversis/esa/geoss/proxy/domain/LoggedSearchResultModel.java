package com.eversis.esa.geoss.proxy.domain;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Logged search result model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedSearchResultModel extends LoggedModel{

    /**
     * The Operation.
     */
    @NotNull
    String operation;

    /**
     * The Ui object id.
     */
    String uiObjectId;

    /**
     * The Ui object class.
     */
    String uiObjectClass;

    /**
     * The Session site url.
     */
    String sessionSiteUrl;

    /**
     * The Ui source.
     */
    String uiSource;

    /**
     * The Ui entryid.
     */
    String uiEntryid;

    /**
     * The Ui action.
     */
    String uiAction;

    /**
     * The Ds query url.
     */
    String dsQueryUrl;

    /**
     * The Ds req id.
     */
    String dsReqId;

    /**
     * The Ds si.
     */
    String dsSi;

    /**
     * The Ds ct.
     */
    String dsCt;

    /**
     * The Ds ts.
     */
    String dsTs;

    /**
     * The Ds te.
     */
    String dsTe;

    /**
     * The Ds st.
     */
    String dsSt;

    /**
     * The Ds rel.
     */
    String dsRel;

    /**
     * The Ds kwd.
     */
    String dsKwd;

    /**
     * The Ds parents group.
     */
    DsParentsGroup dsParentsGroup;

    /**
     * The Ds sba.
     */
    String dsSba;

    /**
     * The Ds sources group.
     */
    DsSourcesGroup dsSourcesGroup;

    /**
     * The Ds views group.
     */
    DsViewsGroup dsViewsGroup;

    /**
     * The Ds bbox.
     */
    String dsBbox;

    /**
     * The Ds gdc.
     */
    String dsGdc;

    /**
     * The Ds w 3 w.
     */
    String dsW3w;

    /**
     * The Ds frmt.
     */
    String dsFrmt;

    /**
     * The Ds prot.
     */
    String dsProt;

    /**
     * The Ds score.
     */
    String dsScore;

}
