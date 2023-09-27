package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.Concept;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.ThesaurusType;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Concept dto.
 */
@Data
@AllArgsConstructor
public class ConceptDto {

    private ThesaurusType source;
    private String term;
    private String termId;
    private boolean isMetaTerm;

    private List<TermDto> relatedConcepts = new ArrayList<>();
    private List<TermDto> narrowerConcepts = new ArrayList<>();
    private List<TermDto> broaderConcepts = new ArrayList<>();

    /**
     * To concept concept.
     *
     * @return the concept
     */
    public Concept toConcept() {
        return Concept.builder()
                .source(source)
                .id(source.name().toLowerCase() + "_" + termId)
                .term(term)
                .definition(null)
                .relatedConcepts(relatedConcepts.stream().map(TermDto::getName).collect(Collectors.toList()))
                .narrowerConcepts(narrowerConcepts.stream().map(TermDto::getName).collect(Collectors.toList()))
                .broaderConcepts(broaderConcepts.stream().map(TermDto::getName).collect(Collectors.toList()))
                .weight(1.0)
                .build();
    }

}
