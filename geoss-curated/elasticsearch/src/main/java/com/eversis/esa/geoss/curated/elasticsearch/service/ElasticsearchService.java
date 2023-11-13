package com.eversis.esa.geoss.curated.elasticsearch.service;

import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
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

    /**
     * Index entry extension.
     *
     * @param entryExtension the entry extension
     */
    void indexEntryExtension(EntryExtension entryExtension);

    /**
     * Remove entry extension from index.
     *
     * @param entryExtension the entry extension
     */
    void removeEntryExtensionFromIndex(EntryExtension entryExtension);

    /**
     * Index dashboard.
     *
     * @param entry the entry
     */
    void indexDashboard(Entry entry);

    /**
     * Remove dashboard from index.
     *
     * @param entry the entry
     */
    void removeDashboardFromIndex(Entry entry);

    /**
     * Reindex entries.
     */
    void reindexEntries();

    /**
     * Reindex entries by type.
     *
     * @param type the type
     */
    void reindexEntriesByType(String type);

    /**
     * Reindex entries by data source.
     *
     * @param dataSource the data source
     */
    void reindexEntriesByDataSource(String dataSource);

    /**
     * Clear entries.
     */
    void clearEntries();

}
