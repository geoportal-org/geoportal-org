package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.cleanup;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.service.EntryGarbageCollectorService;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DefinitionType;

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

    @Value("${geoss.curated.wikipedia.not-found-limit:5}")
    private int notFoundLimit;
    @Value("${geoss.curated.wikipedia.code-prefix}")
    private String entryCodePrefix;

    private final EntryGarbageCollectorService entryGarbageCollectorService;

    /**
     * Instantiates a new Clean up tasklet.
     *
     * @param entryGarbageCollectorService the entry garbage collector service
     */
    public CleanUpTasklet(EntryGarbageCollectorService entryGarbageCollectorService) {
        this.entryGarbageCollectorService = entryGarbageCollectorService;
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
        log.info("Running wikipedia clean up step");
        LocalDateTime jobStartTime = chunkContext.getStepContext().getStepExecution().getJobExecution().getStartTime();

        cleanUpWikipediaEntries(jobStartTime);
        return RepeatStatus.FINISHED;
    }

    private void cleanUpWikipediaEntries(LocalDateTime jobStartTime) {
        entryGarbageCollectorService.run(
                entryCodePrefix,
                DefinitionType.PREDEFINED,
                jobStartTime,
                notFoundLimit
        );
    }

}
