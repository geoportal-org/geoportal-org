package com.eversis.esa.geoss.proxy.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * The type Search result doc.
 */
@Document(indexName = "geoss_index", createIndex = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDoc extends Doc {

    /**
     * The Ui object id.
     */
    @Field(type = FieldType.Keyword, name = "ui_object_id")
    String uiObjectId;

    /**
     * The Ui object class.
     */
    @Field(type = FieldType.Keyword, name = "ui_object_class")
    String uiObjectClass;

    /**
     * The Session site url.
     */
    @Field(type = FieldType.Keyword, name = "session_site_url")
    String sessionSiteUrl;

    /**
     * The Ui source.
     */
    @Field(type = FieldType.Keyword, name = "ui_source")
    String uiSource;

    /**
     * The Ui entryid.
     */
    @Field(type = FieldType.Keyword, name = "ui_entry_id")
    String uiEntryid;

    /**
     * The Ui action.
     */
    @Field(type = FieldType.Keyword, name = "ui_action")
    String uiAction;

    /**
     * The Ds query url.
     */
    @Field(type = FieldType.Keyword, name = "ds_query_url")
    String dsQueryUrl;

    /**
     * The Ds req id.
     */
    @Field(type = FieldType.Keyword, name = "ds_reqID")
    String dsReqId;

    /**
     * The Ds si.
     */
    @Field(type = FieldType.Keyword, name = "ds_si")
    String dsSi;

    /**
     * The Ds ct.
     */
    @Field(type = FieldType.Keyword, name = "ds_ct")
    String dsCt;

    /**
     * The Ds ts.
     */
    @Field(type = FieldType.Keyword, name = "ds_ts")
    String dsTs;

    /**
     * The Ds te.
     */
    @Field(type = FieldType.Keyword, name = "ds_te")
    String dsTe;

    /**
     * The Ds st.
     */
    @Field(type = FieldType.Text, name = "ds_st", fielddata = true)
    String dsSt;

    /**
     * The Ds rel.
     */
    @Field(type = FieldType.Keyword, name = "ds_rel")
    String dsRel;

    /**
     * The Ds kwd.
     */
    @Field(type = FieldType.Keyword, name = "ds_kwd")
    String dsKwd;

    /**
     * The Ds parents group doc.
     */
    @Field(type = FieldType.Auto, name = "ds_parents_group")
    DsParentsGroup dsParentsGroupDoc;

    /**
     * The Ds sba.
     */
    @Field(type = FieldType.Keyword, name = "ds_sba")
    String dsSba;

    /**
     * The Ds sources group doc.
     */
    @Field(type = FieldType.Auto, name = "ds_sources_group")
    DsSourcesGroup dsSourcesGroupDoc;

    /**
     * The Ds views group doc.
     */
    @Field(type = FieldType.Auto, name = "ds_views_group")
    DsViewsGroup dsViewsGroupDoc;

    /**
     * The Ds bbox.
     */
    @Field(type = FieldType.Keyword, name = "ds_bbox")
    String dsBbox;

    /**
     * The Ds gdc.
     */
    @Field(type = FieldType.Keyword, name = "ds_gdc")
    String dsGdc;

    /**
     * The Ds w 3 w.
     */
    @Field(type = FieldType.Keyword, name = "ds_w3w")
    String dsW3w;

    /**
     * The Ds frmt.
     */
    @Field(type = FieldType.Keyword, name = "ds_frmt")
    String dsFrmt;

    /**
     * The Ds prot.
     */
    @Field(type = FieldType.Keyword, name = "ds_prot")
    String dsProt;

    /**
     * The Ds score.
     */
    @Field(type = FieldType.Keyword, name = "ds_score")
    String dsScore;

    /**
     * The Operation.
     */
    @Field(type = FieldType.Keyword, name = "operation")
    String operation;

}
