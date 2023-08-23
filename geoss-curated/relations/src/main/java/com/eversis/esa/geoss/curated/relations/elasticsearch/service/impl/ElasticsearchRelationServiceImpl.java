package com.eversis.esa.geoss.curated.relations.elasticsearch.service.impl;

import com.eversis.esa.geoss.curated.relations.domain.EntryRelation;
import com.eversis.esa.geoss.curated.relations.elasticsearch.service.ElasticsearchRelationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Elasticsearch service.
 */
@Log4j2
@Service
@Transactional
public class ElasticsearchRelationServiceImpl implements ElasticsearchRelationService {

    @Override
    public void indexEntryRelation(EntryRelation entryRelation) {

    }

    @Override
    public void removeEntryRelationFromIndex(EntryRelation entryRelation) {

    }
}
