package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.CkanResultMapper;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * The type Entry ckan response mapper.
 */
@Component
class EntryCkanResponseMapper extends BaseCkanResponseMapper<Entry, Map> {

    /**
     * Instantiates a new Entry ckan response mapper.
     *
     * @param resultMapper the result mapper
     */
    public EntryCkanResponseMapper(CkanResultMapper<Entry, Map> resultMapper) {
        super(resultMapper);
    }

}
