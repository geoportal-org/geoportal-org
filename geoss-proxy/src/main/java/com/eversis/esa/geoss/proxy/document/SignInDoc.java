package com.eversis.esa.geoss.proxy.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * The type Sign in doc.
 */
@Document(indexName = "geoss_index", createIndex = true)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SignInDoc extends Doc {

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
     * The Session site url.
     */
    @Field(type = FieldType.Keyword, name = "session_site_url")
    String sessionSiteUrl;

}
