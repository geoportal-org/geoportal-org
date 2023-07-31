package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.CkanResultMapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The type Base ckan result mapper.
 *
 * @param <T> the type parameter
 * @param <S> the type parameter
 */
abstract class BaseCkanResultMapper<T, S> implements CkanResultMapper<T, S> {

    protected ObjectMapper objectMapper;

    /**
     * Instantiates a new Base ckan result mapper.
     *
     * @param objectMapper the object mapper
     */
    public BaseCkanResultMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        this.objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

}
