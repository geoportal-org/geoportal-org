package com.eversis.esa.geoss.curated.relations.elasticsearch.service;

import com.eversis.esa.geoss.curated.relations.domain.EntryRelation;

/**
 * The interface Elasticsearch service.
 */
public interface ElasticsearchRelationService {

    void indexEntryRelation(EntryRelation entryRelation);

    void removeEntryRelationFromIndex(EntryRelation entryRelation);

}
