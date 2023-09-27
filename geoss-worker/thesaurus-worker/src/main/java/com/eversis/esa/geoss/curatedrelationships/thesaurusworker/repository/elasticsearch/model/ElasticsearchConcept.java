package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.repository.elasticsearch.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * The type Elasticsearch concept.
 */
@Getter
@Setter
@Builder
@Document(indexName = "thesaurus-vocabulary", createIndex = false)
public class ElasticsearchConcept {

    @Id
    private String id;
    @Field(analyzer = "english")
    private String term;
    @Field(type = FieldType.Keyword)
    private String source;
    @Field(analyzer = "english")
    private String definition;
    @Field(type = FieldType.Keyword)
    private List<String> relatedConcepts;
    @Field(type = FieldType.Keyword)
    private List<String> broaderConcepts;
    @Field(type = FieldType.Keyword)
    private List<String> narrowerConcepts;
    @Builder.Default
    @Field(type = FieldType.Double)
    private double weight = 1.0;
}
