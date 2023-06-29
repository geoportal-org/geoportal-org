package com.eversis.esa.geoss.proxy.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * The type Ds views group doc.
 */
@Document(indexName = "geoss_index", createIndex = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DsViewsGroup {

    /**
     * The Key.
     */
    @Field(type = FieldType.Keyword, name = "key")
    String key;

    /**
     * The Value.
     */
    @Field(type = FieldType.Keyword, name = "value")
    String value;

}
