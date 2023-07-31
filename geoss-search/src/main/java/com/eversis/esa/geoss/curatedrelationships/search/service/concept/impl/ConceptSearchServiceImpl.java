package com.eversis.esa.geoss.curatedrelationships.search.service.concept.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.PageRequest;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Concept;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.ConceptElasticsearchRepository;
import com.eversis.esa.geoss.curatedrelationships.search.service.concept.ConceptSearchService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

/**
 * The type Concept search service.
 */
@Slf4j
@Service
@Validated
class ConceptSearchServiceImpl implements ConceptSearchService {

    private final ConceptElasticsearchRepository thesaurusRepository;

    /**
     * Instantiates a new Concept search service.
     *
     * @param thesaurusRepository the thesaurus repository
     */
    @Autowired
    public ConceptSearchServiceImpl(ConceptElasticsearchRepository thesaurusRepository) {
        this.thesaurusRepository = thesaurusRepository;
    }

    @Override
    public List<String> findConceptNames(@NotNull String phrase, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count parameter may not be lesser than zero");
        }
        List<Concept> concepts = thesaurusRepository.findConcepts(phrase, new PageRequest(0, count)).getContent();
        List<String> conceptsNames = new ArrayList<>(concepts.size());

        for (Concept concept : concepts) {
            if (conceptsNames.size() >= count) {
                break;
            }
            conceptsNames.addAll(concept.getRelatedConcepts());
        }
        for (Concept concept : concepts) {
            if (conceptsNames.size() >= count) {
                break;
            }
            conceptsNames.addAll(concept.getBroaderConcepts());
        }
        for (Concept concept : concepts) {
            if (conceptsNames.size() >= count) {
                break;
            }
            conceptsNames.addAll(concept.getNarrowerConcepts());
        }

        return conceptsNames.stream()
                .filter(conceptName -> !conceptName.equalsIgnoreCase(phrase))
                .distinct()
                .limit(Math.min(count, conceptsNames.size()))
                .collect(Collectors.toList());
    }

}
