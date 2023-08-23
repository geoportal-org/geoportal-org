package com.eversis.esa.geoss.curated.relations.elasticsearch.service;

import com.eversis.esa.geoss.curated.relations.domain.EntryRelation;

/**
 * The interface Elasticsearch service.
 */
public interface ElasticsearchRelationService {

    /**
     * Index entry relation.
     *
     * @param entryRelation the entry relation
     */
    void indexEntryRelation(EntryRelation entryRelation);

    /**
     * Remove entry relation from index.
     *
     * @param entryRelation the entry relation
     */
    void removeEntryRelationFromIndex(EntryRelation entryRelation);

}
