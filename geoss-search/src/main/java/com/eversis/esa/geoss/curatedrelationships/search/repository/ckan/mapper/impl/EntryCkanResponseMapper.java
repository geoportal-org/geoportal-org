package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.CkanResultMapper;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
class EntryCkanResponseMapper extends BaseCkanResponseMapper<Entry, Map> {

    public EntryCkanResponseMapper(CkanResultMapper<Entry, Map> resultMapper) {
        super(resultMapper);
    }

}
