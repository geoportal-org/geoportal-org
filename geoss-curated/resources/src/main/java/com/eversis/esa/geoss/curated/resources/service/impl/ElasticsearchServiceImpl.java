package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * The type Elasticsearch service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Validated
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Override
    public void indexEntry(long entryId) {

    }

    @Override
    public void updateEntry(long entryId) {

    }

    @Override
    public void removeEntryFromIndex(long entryId) {

    }

}
