package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.webflux;

import lombok.Data;

/**
 * The type Thesaurus job model.
 */
@Data
public class ThesaurusJobModel {

    private String name;

    private String status;

    private Long count;
}
