package com.eversis.esa.geoss.curated.elasticsearch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

/**
 * The type Recommendation elk.
 */
@Document(indexName = "geoss-recommendation", createIndex = false)
@Data
public class RecommendationELK implements Serializable {

    @Id
    @Field(type = FieldType.Text)
    private String term;

    @Version
    @Field(name = "@version", type = FieldType.Keyword)
    private Long version = 1L;

    @Field(
            name = "@timestamp", type = FieldType.Date,
            format = {},
            pattern = {"yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSz", "yyyy-MM-dd'T'HH:mm:ss.SSSSSSz"}
    )
    private Instant timestamp = Instant.now();

    @Field(type = FieldType.Keyword)
    private Long recommendationId;

    private Set<RecommendedResourceELK> entities;

    /**
     * Increment long.
     *
     * @return the long
     */
    public synchronized Long increment() {
        return ++version;
    }
}
