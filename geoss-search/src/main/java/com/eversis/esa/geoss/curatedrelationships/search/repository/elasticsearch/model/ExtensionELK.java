package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExtensionELK {

    private String dataSource;
    private String entryCode;

    private List<TransferOptionExtensionELK> transferOptions = new ArrayList<>();
    private List<EntryExtensionELK> extensions = new ArrayList<>();
    private List<EntryRelationELK> relations = new ArrayList<>();

}
