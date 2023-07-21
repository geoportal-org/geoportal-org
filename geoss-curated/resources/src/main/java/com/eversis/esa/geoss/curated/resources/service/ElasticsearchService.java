package com.eversis.esa.geoss.curated.resources.service;

/**
 * The interface Elasticsearch service.
 */
public interface ElasticsearchService {

    /**
     * Index entry.
     *
     * @param entryId the entry id
     */
    void indexEntry(long entryId);

    /**
     * Update entry.
     *
     * @param entryId the entry id
     */
    void updateEntry(long entryId);

    /**
     * Remove entry from index.
     *
     * @param entryId the entry id
     */
    void removeEntryFromIndex(long entryId);

}
