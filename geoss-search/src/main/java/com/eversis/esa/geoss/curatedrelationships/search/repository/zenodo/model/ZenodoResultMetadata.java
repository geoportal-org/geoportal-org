package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.model;

import lombok.Data;

import java.util.List;

@Data
class ZenodoResultMetadata {

    private String title;
    private String description;
    private List<ZenodoCreator> creators;
    private List<String> keywords;

}
