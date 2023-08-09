package com.eversis.esa.geoss.curated.resources.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.geo.GeoJsonMultiPoint;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Resource entry elk.
 */
@Document(indexName = "geoss-cr", createIndex = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceEntryELK {

    @Id
    private String id;

    @Field(type = FieldType.Keyword, name = "title")
    private String title;

    @Field(type = FieldType.Keyword, name = "summary")
    private String summary;

    @Field(type = FieldType.Keyword, name = "logo")
    private String logo;

    @Field(type = FieldType.Object, name = "coverage")
    private GeoJsonMultiPoint coverage;

    @Field(type = FieldType.Keyword, name = "keywords")
    private List<String> keywords = new ArrayList<>();

    @Field(type = FieldType.Keyword, name = "tags")
    private List<String> tags = new ArrayList<>();

    @Field(type = FieldType.Keyword, name = "code")
    private String code;

    @Field(type = FieldType.Date, name = "publishDate")
    private LocalDateTime publishDate;

    @Field(type = FieldType.Object, name = "source")
    private SourceELK source;

    @Field(type = FieldType.Keyword, name = "type")
    private String type;

    @Field(type = FieldType.Keyword, name = "dataSource")
    private String dataSource;

    @Field(type = FieldType.Keyword, name = "displayDataSource")
    private String displayDataSource;

    @Field(type = FieldType.Keyword, name = "scoreWeight")
    private double scoreWeight;

    @Field(type = FieldType.Object, name = "organisation")
    private OrganisationELK organisation;

    @Field(type = FieldType.Object, name = "accessPolicy")
    private AccessPolicyELK accessPolicy;

    @Field(type = FieldType.Object, name = "dashboardContents")
    private DashboardContentsELK dashboardContents;

}
