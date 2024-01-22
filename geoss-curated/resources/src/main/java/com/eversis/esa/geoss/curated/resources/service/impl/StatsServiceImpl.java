package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.EntryStats;
import com.eversis.esa.geoss.curated.resources.repository.EntryStatsRepository;
import com.eversis.esa.geoss.curated.resources.service.StatsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Stats service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final EntryStatsRepository entryStatsRepository;

    /**
     * Instantiates a new Stats service.
     *
     * @param entryStatsRepository the entry stats repository
     */
    public StatsServiceImpl(EntryStatsRepository entryStatsRepository) {
        this.entryStatsRepository = entryStatsRepository;
    }

    @Override
    public EntryStats findStatsByTargetIdAndDataSource(String targetId, String dataSource) {
        return entryStatsRepository.findByTargetIdAndDataSource(targetId, dataSource);
    }

    @Override
    public EntryStats addOrUpdateStats(String targetId, String dataSource, Double score) {
        EntryStats entryStats = entryStatsRepository.findByTargetIdAndDataSource(targetId, dataSource);
        if (entryStats == null) {
            entryStats = new EntryStats();
            entryStats.setTargetId(targetId);
            entryStats.setDataSource(dataSource);
            entryStats.setTotalEntries(1);
            entryStats.setTotalScore(score);
            entryStats.setAverageScore(score);
            entryStats = entryStatsRepository.save(entryStats);
            log.info("Created new entry stats {}", entryStats);
        } else {
            entryStats.setTotalEntries(entryStats.getTotalEntries() + 1);
            entryStats.setTotalScore(entryStats.getTotalScore() + score);
            entryStats.setAverageScore(entryStats.getTotalScore() / entryStats.getTotalEntries());
            entryStats = entryStatsRepository.save(entryStats);
            log.info("Updated entry stats {}", entryStats);
        }
        return entryStats;
    }

}
