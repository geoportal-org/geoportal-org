package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.EntryStats;

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

}
