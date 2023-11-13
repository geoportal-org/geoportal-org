package com.eversis.esa.geoss.curated.elasticsearch.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * The type Entry extension elk.
 */
@Document(indexName = "geoss-cr-extensions", createIndex = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryExtensionELK {

    @Id
    private String id;

    @Field(type = FieldType.Keyword, name = "dataSource")
    private String dataSource;

    @Field(type = FieldType.Keyword, name = "entryCode")
    private String entryCode;

    @Field(type = FieldType.Object, name = "transferOptions")
    private List<TransferOptionExtensionELK> transferOptions = new ArrayList<>();

}
