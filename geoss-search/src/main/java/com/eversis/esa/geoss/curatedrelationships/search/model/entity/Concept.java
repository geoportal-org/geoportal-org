package com.eversis.esa.geoss.curatedrelationships.search.model.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Concept.
 */
@Data
public class Concept {

    private String term;

    private List<String> relatedConcepts = new ArrayList<>();
    private List<String> broaderConcepts = new ArrayList<>();
    private List<String> narrowerConcepts = new ArrayList<>();

}
