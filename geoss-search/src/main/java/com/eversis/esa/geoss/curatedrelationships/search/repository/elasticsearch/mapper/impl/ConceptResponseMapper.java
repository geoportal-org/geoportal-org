package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Concept;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchDocumentMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConceptResponseMapper extends BaseElasticsearchResponseMapper<Concept> {

    @Autowired
    public ConceptResponseMapper(ElasticsearchDocumentMapper<Concept> documentMapper) {
        super(documentMapper);
    }

}
