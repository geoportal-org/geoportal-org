package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.repository.elasticsearch.repository;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.repository.elasticsearch.model.ElasticsearchConcept;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Reactive elasticsearch concept repository.
 */
@Repository
public interface ReactiveElasticsearchConceptRepository extends ReactiveCrudRepository<ElasticsearchConcept, String> {

}
