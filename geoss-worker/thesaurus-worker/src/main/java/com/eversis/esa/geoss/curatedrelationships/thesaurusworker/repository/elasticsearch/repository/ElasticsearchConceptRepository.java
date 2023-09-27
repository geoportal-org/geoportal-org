package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.repository.elasticsearch.repository;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.Concept;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.repository.ConceptRepository;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.repository.elasticsearch.mapper.ConceptMapper;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.repository.elasticsearch.model.ElasticsearchConcept;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;

/**
 * The type Elasticsearch concept repository.
 */
@Repository
public class ElasticsearchConceptRepository implements ConceptRepository {

    private final ReactiveElasticsearchConceptRepository reactiveElasticsearchConceptRepository;

    /**
     * Instantiates a new Elasticsearch concept repository.
     *
     * @param reactiveElasticsearchConceptRepository the reactive elasticsearch concept repository
     */
    @Autowired
    public ElasticsearchConceptRepository(
            ReactiveElasticsearchConceptRepository reactiveElasticsearchConceptRepository) {
        this.reactiveElasticsearchConceptRepository = reactiveElasticsearchConceptRepository;
    }

    /**
     * Save concept.
     *
     * @param concept the concept
     */
    @Override
    public void saveConcept(@NotNull Concept concept) {
        ElasticsearchConcept elasticsearchConcept = ConceptMapper.mapConcept(concept);
        reactiveElasticsearchConceptRepository.save(elasticsearchConcept).subscribe();
    }

}
