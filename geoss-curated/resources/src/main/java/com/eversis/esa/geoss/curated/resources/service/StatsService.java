package com.eversis.esa.geoss.curated.resources.service;

import java.util.List;

import com.eversis.esa.geoss.curated.resources.domain.EntryStats;
import com.eversis.esa.geoss.curated.resources.model.EntryStatsModel;

/**
 * The interface Stats service.
 */
public interface StatsService {

    /**
     * Find stats by target id and data source entry stats.
     *
     * @param targetId the target id
     * @param dataSource the data source
     * @return the entry stats
     */
    EntryStats findStatsByTargetIdAndDataSource(String targetId, String dataSource);

    /**
     * Add or update stats entry stats.
     *
     * @param targetId the target id
     * @param dataSource the data source
     * @param score the score
     * @return the entry stats
     */
    EntryStats addOrUpdateStats(String targetId, String dataSource, Double score);

    /**
     * Find stats list list.
     *
     * @return the list
     */
    List<EntryStats> findStatsList();

    /**
     * Find stats entry stats.
     *
     * @param statsId the stats id
     * @return the entry stats
     */
    EntryStats findStats(long statsId);

    /**
     * Create stats.
     *
     * @param entryStatsDto the entry stats dto
     */
    void createStats(EntryStatsModel entryStatsDto);

    /**
     * Update stats.
     *
     * @param statsId the stats id
     * @param entryStatsDto the entry stats dto
     */
    void updateStats(long statsId, EntryStatsModel entryStatsDto);

    /**
     * Delete stats.
     *
     * @param statsId the stats id
     */
    void deleteStats(long statsId);

    /**
     * Delete all stats.
     */
    void deleteAllStats();
}
