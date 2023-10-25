package com.eversis.esa.geoss.curated.elasticsearch.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * The type Recommended resource elk.
 */
@Data
public class RecommendedResourceELK implements Serializable {

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String code;

    @Field(type = FieldType.Keyword)
    private String dataSource;

    @Field
    private double orderWeight = 1.0;
}
