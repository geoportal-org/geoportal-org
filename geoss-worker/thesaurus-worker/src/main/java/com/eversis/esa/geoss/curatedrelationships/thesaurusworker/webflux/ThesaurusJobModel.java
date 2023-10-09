package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.webflux;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.ThesaurusType;

import lombok.Data;

/**
 * The type Thesaurus job model.
 */
@Data
public class ThesaurusJobModel {

    private ThesaurusType type;

    private String status;

    private Long count;
}
