package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.cleanup;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.service.EntryGarbageCollectorService;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DefinitionType;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.DabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.VlabDabProperties;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * The type Clean up tasklet.
 */
@Log4j2
@StepScope
@Component
public class CleanUpTasklet implements Tasklet {

    @Value("${geoss.curated.not-found-limit:5}")
    private int notFoundLimit;

    private final EntryGarbageCollectorService entryGarbageCollectorService;
    private final DabProperties dabProperties;
    private final VlabDabProperties vlabDabProperties;

    /**
     * Instantiates a new Clean up tasklet.
     *
     * @param entryGarbageCollectorService the entry garbage collector service
     * @param dabProperties the dab configuration
     * @param vlabDabProperties the vlab dab configuration
     */
    public CleanUpTasklet(
            EntryGarbageCollectorService entryGarbageCollectorService,
            DabProperties dabProperties,
            VlabDabProperties vlabDabProperties) {
        this.entryGarbageCollectorService = entryGarbageCollectorService;
        this.dabProperties = dabProperties;
        this.vlabDabProperties = vlabDabProperties;
    }

    /**
     * Execute repeat status.
     *
     * @param contribution the contribution
     * @param chunkContext the chunk context
     * @return the repeat status
     * @throws Exception the exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Running geodab clean up step");
        LocalDateTime jobStartTime = chunkContext.getStepContext().getStepExecution().getJobExecution().getStartTime();

        cleanUpGeoDabEntries(jobStartTime);
        cleanUpVlabRunsEntries(jobStartTime);
        return RepeatStatus.FINISHED;
    }

    private void cleanUpGeoDabEntries(LocalDateTime jobStartTime) {
        entryGarbageCollectorService.run(
                dabProperties.getEntryCodePrefix(),
                DefinitionType.PREDEFINED,
                jobStartTime,
                notFoundLimit
        );
    }

    private void cleanUpVlabRunsEntries(LocalDateTime jobStartTime) {
        entryGarbageCollectorService.run(
                vlabDabProperties.getRuns().getEntryCodePrefix(),
                DefinitionType.PREDEFINED,
                jobStartTime,
                notFoundLimit
        );
    }

}
