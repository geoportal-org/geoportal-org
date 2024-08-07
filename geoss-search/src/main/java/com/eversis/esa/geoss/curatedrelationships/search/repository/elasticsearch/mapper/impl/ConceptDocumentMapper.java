package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Concept;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * The type Concept document mapper.
 */
@Log4j2
@Component
public class ConceptDocumentMapper extends BaseElasticsearchDocumentMapper<Concept> {

    /**
     * Instantiates a new Concept document mapper.
     *
     * @param objectMapper the object mapper
     */
    @Autowired
    public ConceptDocumentMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Concept mapToObject(SearchHit searchHit) throws IOException {
        Map<String, Object> source = searchHit != null ? searchHit.getSourceAsMap() : Collections.emptyMap();
        log.debug("sourceConcept:{}", source);
        return objectMapper.convertValue(source, Concept.class);
    }
}
