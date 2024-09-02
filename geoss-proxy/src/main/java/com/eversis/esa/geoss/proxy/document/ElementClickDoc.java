package com.eversis.esa.geoss.proxy.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * The type Element click doc.
 */
@Document(indexName = "geoss_index", createIndex = true)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ElementClickDoc extends Doc {

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
     * The Ui source.
     */
    @Field(type = FieldType.Keyword, name = "ui_source")
    String uiSource;

    /**
     * The Session site url.
     */
    @Field(type = FieldType.Keyword, name = "session_site_url")
    String sessionSiteUrl;

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
     * The Ui label.
     */
    @Field(type = FieldType.Keyword, name = "ui_label")
    String uiLabel;

    /**
     * The Ui organisation.
     */
    @Field(type = FieldType.Keyword, name = "ui_organisation")
    String uiOrganisation;

    /**
     * The Ui resource name.
     */
    @Field(type = FieldType.Keyword, name = "ui_resource_name")
    String uiResourceName;

    /**
     * The Operation.
     */
    @Field(type = FieldType.Keyword, name = "operation")
    String operation;

}
