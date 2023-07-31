package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchDocumentMapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

abstract class BaseElasticsearchDocumentMapper<T> implements ElasticsearchDocumentMapper<T> {

    protected ObjectMapper objectMapper;

    public BaseElasticsearchDocumentMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        this.objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @Override
    public String mapToString(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

}
