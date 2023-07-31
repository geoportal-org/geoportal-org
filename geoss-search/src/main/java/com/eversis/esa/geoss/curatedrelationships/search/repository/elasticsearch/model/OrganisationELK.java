package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganisationELK {

    private String id;
    private String title;
    private String contact;
    private String email;
    private String contactEmail;
    private String role;

}
