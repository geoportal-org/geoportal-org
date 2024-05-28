package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.resources.domain.EntryStats;
import com.eversis.esa.geoss.curated.resources.model.EntryStatsModel;
import org.springframework.stereotype.Component;

/**
 * The type Entry stats mapper.
 */
@Component
public class EntryStatsMapper {

    /**
     * Map to entry stats entry stats.
     *
     * @param model the model
     * @return the entry stats
     */
    public EntryStats mapToEntryStats(EntryStatsModel model) {
        return getEntryStats(model);
    }

    /**
     * Map to entry stats entry stats.
     *
     * @param model the model
     * @param entryStats the entry stats
     * @return the entry stats
     */
    public EntryStats mapToEntryStats(EntryStatsModel model, EntryStats entryStats) {
        return getEntryStats(model, entryStats);
    }

    private EntryStats getEntryStats(EntryStatsModel model) {
        if (model == null) {
            return null;
        }
        EntryStats entryStats = new EntryStats();
        entryStats.setTargetId(model.getTargetId());
        entryStats.setTotalEntries(model.getTotalEntries());
        entryStats.setTotalScore(model.getTotalScore());
        entryStats.setAverageScore(model.getAverageScore());
        entryStats.setDataSource(model.getDataSource());
        return entryStats;
    }

    private EntryStats getEntryStats(EntryStatsModel model, EntryStats entryStats) {
        if (model == null) {
            return null;
        }
        entryStats.setTargetId(model.getTargetId());
        entryStats.setTotalEntries(model.getTotalEntries());
        entryStats.setTotalScore(model.getTotalScore());
        entryStats.setAverageScore(model.getAverageScore());
        entryStats.setDataSource(model.getDataSource());
        return entryStats;
    }
}
