package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.repository.common.exception.ResultParsingException;
import com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.mapper.ZenodoMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.model.ZenodoResult;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class EntryZenodoMapper implements ZenodoMapper<Entry> {

    private final ZenodoResultMapper<Entry, ZenodoResult> resultMapper;

    public EntryZenodoMapper(ZenodoResultMapper<Entry, ZenodoResult> resultMapper) {
        this.resultMapper = resultMapper;
    }

    @Override
    public Entry mapSearchResult(ZenodoResult result) {
        try {
            return resultMapper.mapToObject(result);
        } catch (IOException e) {
            throw new ResultParsingException("Unable to process result from Zenodo : ", e);
        }
    }
}
