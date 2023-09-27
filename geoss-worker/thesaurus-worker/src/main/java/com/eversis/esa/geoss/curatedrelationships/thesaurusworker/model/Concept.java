package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Concept.
 */
@Data
@Builder
public class Concept {

    private ThesaurusType source;
    private String id;
    private String term;
    private String definition;

    @Builder.Default
    private List<String> relatedConcepts = new ArrayList<>();
    @Builder.Default
    private List<String> narrowerConcepts = new ArrayList<>();
    @Builder.Default
    private List<String> broaderConcepts = new ArrayList<>();
    @Builder.Default
    private double weight = 1.0;
}
