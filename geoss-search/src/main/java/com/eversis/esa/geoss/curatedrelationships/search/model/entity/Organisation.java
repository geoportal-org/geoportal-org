package com.eversis.esa.geoss.curatedrelationships.search.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Organisation implements Serializable {

    private String id;
    private String title;
    private String contact;
    private String email;
    private String contactEmail;
    private String role;

}
