package com.eversis.esa.geoss.proxy.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * The type Ds sources group doc.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DsSourcesGroup {

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
