package com.eversis.esa.geoss.personaldata.survey.jpa;

import lombok.Data;

/**
 * The type Search criteria.
 */
@Data
public class SearchCriteria {

    private String key;
    private SearchOperation operation;
    private String value;
}
