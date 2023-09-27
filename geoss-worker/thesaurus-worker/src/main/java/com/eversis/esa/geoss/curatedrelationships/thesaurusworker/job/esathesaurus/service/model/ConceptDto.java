package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.model;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.Concept;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.ThesaurusType;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Concept dto.
 */
@Getter
@Setter
public class ConceptDto extends BaseConceptDto {

    private String definition;

    private List<BaseConceptDto> relatedConcepts = new ArrayList<>();
    private List<BaseConceptDto> broaderConcepts = new ArrayList<>();
    private List<BaseConceptDto> narrowerConcepts = new ArrayList<>();

    /**
     * Instantiates a new Concept dto.
     *
     * @param term the term
     * @param uri the uri
     */
    public ConceptDto(String term, String uri) {
        super(term, uri);
    }

    /**
     * Instantiates a new Concept dto.
     *
     * @param term the term
     * @param uri the uri
     * @param definition the definition
     * @param relatedConcepts the related concepts
     * @param broaderConcepts the broader concepts
     * @param narrowerConcepts the narrower concepts
     */
    public ConceptDto(String term, String uri, String definition,
            List<BaseConceptDto> relatedConcepts,
            List<BaseConceptDto> broaderConcepts,
            List<BaseConceptDto> narrowerConcepts) {
        super(term, uri);
        this.definition = definition;
        this.relatedConcepts = relatedConcepts;
        this.broaderConcepts = broaderConcepts;
        this.narrowerConcepts = narrowerConcepts;
    }

    /**
     * To concept concept.
     *
     * @return the concept
     */
    public Concept toConcept() {
        return Concept.builder()
                .source(ThesaurusType.ESA)
                .id(ThesaurusType.ESA.name().toLowerCase() + "_" + uri)
                .term(term)
                .definition(definition)
                .relatedConcepts(relatedConcepts.stream().map(BaseConceptDto::getTerm).collect(Collectors.toList()))
                .narrowerConcepts(narrowerConcepts.stream().map(BaseConceptDto::getTerm).collect(Collectors.toList()))
                .broaderConcepts(broaderConcepts.stream().map(BaseConceptDto::getTerm).collect(Collectors.toList()))
                .weight(2.0)
                .build();
    }
}
