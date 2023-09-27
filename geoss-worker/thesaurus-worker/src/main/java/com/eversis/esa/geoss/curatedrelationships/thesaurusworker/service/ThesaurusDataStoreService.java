package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.service;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.Concept;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.repository.elasticsearch.repository.ElasticsearchConceptRepository;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;

/**
 * The type Thesaurus data store service.
 */
@Log4j2
@Service
public class ThesaurusDataStoreService {

    private final ElasticsearchConceptRepository conceptRepository;

    /**
     * Instantiates a new Thesaurus data store service.
     *
     * @param conceptRepository the concept repository
     */
    @Autowired
    public ThesaurusDataStoreService(
            ElasticsearchConceptRepository conceptRepository) {
        this.conceptRepository = conceptRepository;
    }

    /**
     * Save concept.
     *
     * @param concept the concept
     */
    public void saveConcept(@NotNull Concept concept) {
        if (log.isTraceEnabled()) {
            log.trace("Saving concept: {}", concept);
        }
        conceptRepository.saveConcept(concept);
    }

}
