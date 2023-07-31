package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DatasetCkan {

    private String id;
    private String title;
    private String notes;

    private String maintainer;
    @JsonProperty("maintainer_email")
    private String maintainerEmail;

    @JsonProperty("metadata_modified")
    private LocalDateTime modifiedDate;

    private String type;
    private OrganizationCkan organization;

    private List<DatasetResourceCkan> resources;
    private List<TagCkan> tags;

}
