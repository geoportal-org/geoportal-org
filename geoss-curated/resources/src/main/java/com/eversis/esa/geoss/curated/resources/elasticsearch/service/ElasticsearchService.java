package com.eversis.esa.geoss.curated.resources.elasticsearch.service;

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

}
