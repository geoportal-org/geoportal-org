package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.EntryStats;
import com.eversis.esa.geoss.curated.resources.mapper.EntryStatsMapper;
import com.eversis.esa.geoss.curated.resources.model.EntryStatsModel;
import com.eversis.esa.geoss.curated.resources.repository.EntryStatsRepository;
import com.eversis.esa.geoss.curated.resources.service.StatsService;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Stats service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final EntryStatsRepository entryStatsRepository;
    private final EntryStatsMapper entryStatsMapper;

    /**
     * Instantiates a new Stats service.
     *
     * @param entryStatsRepository the entry stats repository
     * @param entryStatsMapper the entry stats mapper
     */
    public StatsServiceImpl(EntryStatsRepository entryStatsRepository, EntryStatsMapper entryStatsMapper) {
        this.entryStatsRepository = entryStatsRepository;
        this.entryStatsMapper = entryStatsMapper;
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

    @Override
    public List<EntryStats> findStatsList() {
        return entryStatsRepository.findAll();
    }

    @Override
    public EntryStats findStats(long statsId) {
        return entryStatsRepository.findById(statsId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Entry stats with statsId: " + statsId + " does not exist"));
    }

    @Transactional
    @Override
    public void createStats(EntryStatsModel entryStatsDto) {
        entryStatsRepository.save(entryStatsMapper.mapToEntryStats(entryStatsDto));
    }

    @Transactional
    @Override
    public void updateStats(long statsId, EntryStatsModel entryStatsDto) {
        final EntryStats entryStats = entryStatsRepository.findById(statsId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Entry stats entity with statsId: " + statsId + " does not exist"));
        entryStatsRepository.save(entryStatsMapper.mapToEntryStats(entryStatsDto, entryStats));
    }

    @Transactional
    @Override
    public void deleteStats(long statsId) {
        entryStatsRepository.deleteById(statsId);
    }

    @Transactional
    @Override
    public void deleteAllStats() {
        entryStatsRepository.deleteAll();
    }

}
