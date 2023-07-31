package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Concept;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchDocumentMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Concept response mapper.
 */
@Component
public class ConceptResponseMapper extends BaseElasticsearchResponseMapper<Concept> {

    /**
     * Instantiates a new Concept response mapper.
     *
     * @param documentMapper the document mapper
     */
    @Autowired
    public ConceptResponseMapper(ElasticsearchDocumentMapper<Concept> documentMapper) {
        super(documentMapper);
    }

}
