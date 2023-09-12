package com.eversis.esa.geoss.curated.elasticsearch.service;

import com.eversis.esa.geoss.curated.relations.domain.EntryRelation;
import com.eversis.esa.geoss.curated.resources.domain.Entry;

/**
 * The interface Elasticsearch service.
 */
public interface ElasticsearchService {

    /**
     * Index entry.
     *
     * @param entry the entry
     */
    void indexEntry(Entry entry);

    /**
     * Remove entry from index.
     *
     * @param entry the entry
     */
    void removeEntryFromIndex(Entry entry);

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
