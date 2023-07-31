package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.mapper.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

abstract class BaseZenodoResultMapper<T, S> implements ZenodoResultMapper<T, S> {

    protected ObjectMapper objectMapper;

    public BaseZenodoResultMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        this.objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

}
