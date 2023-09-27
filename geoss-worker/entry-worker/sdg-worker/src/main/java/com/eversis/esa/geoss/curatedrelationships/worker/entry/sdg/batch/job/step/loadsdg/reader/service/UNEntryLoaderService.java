package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.EntryRelation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.un.UNGoal;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.un.UNIndicator;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.un.UNSeries;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.un.UNTarget;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.mapper.UNEntryMapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

/**
 * The type Un entry loader service.
 */
@Log4j2
@Service
class UNEntryLoaderService {

    private final WebClient webClient;
    private final UNQueryFactory queryFactory;
    private final UNEntryMapper entryMapper;

    /**
     * Instantiates a new Un entry loader service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     * @param entryMapper the entry mapper
     */
    @Autowired
    public UNEntryLoaderService(@Qualifier("sdgUnWebClient") WebClient webClient,
            UNQueryFactory queryFactory,
            UNEntryMapper entryMapper) {
        this.webClient = webClient;
        this.queryFactory = queryFactory;
        this.entryMapper = entryMapper;
    }

    /**
     * Fetch entries list.
     *
     * @return the list
     */
    public List<Entry> fetchEntries() {
        List<UNGoal> goals = fetchUNEntries();

        List<Entry> entries = new LinkedList<>();

        for (UNGoal goal : goals) {
            Entry goalEntry = entryMapper.createUNGoalEntry(goal);
            entries.add(goalEntry);

            for (UNTarget target : goal.getTargets()) {
                Entry targetEntry = entryMapper.createUNTargetEntry(target);
                EntryRelation goalTargetRelation = entryMapper.createSdgEntryRelation(
                        goal.getCode(),
                        goalEntry.getType(),
                        target.getCode(),
                        targetEntry.getType());
                targetEntry.addRelation(goalTargetRelation);
                entries.add(targetEntry);

                for (UNIndicator indicator : target.getIndicators()) {
                    Entry indicatorEntry = entryMapper.createUNIndicatorEntry(indicator);
                    EntryRelation targetIndicatorRelation = entryMapper.createSdgEntryRelation(
                            target.getCode(),
                            targetEntry.getType(),
                            indicator.getCode(),
                            indicatorEntry.getType());
                    indicatorEntry.addRelation(targetIndicatorRelation);

                    for (UNSeries series : indicator.getSeries()) {
                        TransferOption transferOption = entryMapper.createUNSeriesTransferOption(series);
                        indicatorEntry.addTransferOption(transferOption);
                    }
                    entries.add(indicatorEntry);
                }
            }
        }
        log.info("Fetched {} entries directly from UN", entries.size());
        return entries;
    }

    private List<UNGoal> fetchUNEntries() {
        return webClient
                .get()
                .uri(queryFactory.createUNEntriesUriQuery())
                .retrieve()
                .bodyToFlux(UNGoal.class)
                .timeout(Duration.ofSeconds(30))
                .collectList()
                .block();
    }
}
