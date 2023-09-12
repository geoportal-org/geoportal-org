package com.eversis.esa.geoss.curated.elasticsearch.model;

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

    @Field(type = FieldType.Keyword, name = "parentId")
    private List<String> parentId = new ArrayList<>();
    // ["geoss_cr_un_sd_16.8","geoss_cr_un_sd_16.8"] - czyli tutaj aktualizujeme entry które jest child

    @Field(type = FieldType.Boolean, name = "hasChildren")
    private boolean hasChildren;  //   "hasChildren": "false" lub "true" - aktualizujeme entry które jest parent

//        if ([child_count] > 0){
//        mutate {
//            replace => [ "child_count", "true" ]
//        }
//    } else {
//        mutate {
//            replace => [ "child_count", "false" ]
//        }
//    }

//    map['hasChildren'] = event.get('child_count')

    @Field(type = FieldType.Keyword, name = "childrenTypes")
    private String childrenTypes;
//    map['childrenTypes'] = event.get('children_types')  - czyli tutaj aktualizujeme entry które jest parent
//"childrenTypes": "data_resource",
    // do sprawdzenia ale jak jest kilka różnych childrentype to bedzie String po przecinku "childrenTypes": "data_resource, information_resource",

}
