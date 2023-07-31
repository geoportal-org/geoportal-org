package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrganizationCkan {

    private String id;
    private String title;
    private String description;
    @JsonProperty("image_url")
    private String imgUrl;

}
