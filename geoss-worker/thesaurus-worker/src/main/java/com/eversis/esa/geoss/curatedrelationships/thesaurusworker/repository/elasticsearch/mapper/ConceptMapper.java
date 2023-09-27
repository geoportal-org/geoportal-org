package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.repository.elasticsearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.Concept;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.repository.elasticsearch.model.ElasticsearchConcept;

import lombok.NonNull;

import java.util.stream.Collectors;

/**
 * The type Concept mapper.
 */
public class ConceptMapper {

    private ConceptMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map concept elasticsearch concept.
     *
     * @param concept the concept
     * @return the elasticsearch concept
     */
    public static ElasticsearchConcept mapConcept(@NonNull Concept concept) {
        return ElasticsearchConcept.builder()
                .id(concept.getId())
                .source(concept.getSource().name().toLowerCase())
                .term(concept.getTerm() != null ? concept.getTerm().toLowerCase() : null)
                .definition(concept.getDefinition() != null ? concept.getDefinition().toLowerCase() : null)
                .broaderConcepts(
                        concept.getBroaderConcepts().stream().map(String::toLowerCase).collect(Collectors.toList()))
                .narrowerConcepts(
                        concept.getNarrowerConcepts().stream().map(String::toLowerCase).collect(Collectors.toList()))
                .relatedConcepts(
                        concept.getRelatedConcepts().stream().map(String::toLowerCase).collect(Collectors.toList()))
                .weight(concept.getWeight())
                .build();
    }

}
