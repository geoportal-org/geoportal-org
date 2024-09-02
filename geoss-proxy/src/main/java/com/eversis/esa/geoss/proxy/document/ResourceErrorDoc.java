package com.eversis.esa.geoss.proxy.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * The type Resource error doc.
 */
@Document(indexName = "geoss_index", createIndex = true)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResourceErrorDoc extends Doc {

    /**
     * The Ui entry id.
     */
    @Field(type = FieldType.Keyword, name = "ui_entry_id")
    String uiEntryId;

    /**
     * The Result.
     */
    @Field(type = FieldType.Keyword, name = "result")
    String result;

    /**
     * The Operation.
     */
    @Field(type = FieldType.Keyword, name = "operation")
    String operation;

    /**
     * The Message.
     */
    @Field(type = FieldType.Text, name = "message")
    String message;

    /**
     * The Result details.
     */
    @Field(type = FieldType.Keyword, name = "result_details")
    String resultDetails;

    /**
     * The Session site url.
     */
    @Field(type = FieldType.Keyword, name = "session_site_url")
    String sessionSiteUrl;

    /**
     * The Short url.
     */
    @Field(type = FieldType.Keyword, name = "short_url")
    String shortUrl;

}
