package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Concept;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
public class ConceptDocumentMapper extends BaseElasticsearchDocumentMapper<Concept> {

    @Autowired
    public ConceptDocumentMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Concept mapToObject(SearchHit searchHit) throws IOException {
        Map source = searchHit != null ? searchHit.getSourceAsMap() : Collections.emptyMap();
        return objectMapper.convertValue(source, Concept.class);
    }
}
