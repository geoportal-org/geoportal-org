package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

/**
 * The type Sdg entry chunk listener.
 */
@Log4j2
public class SdgEntryChunkListener implements ChunkListener {

    private static final int LOGGING_INTERVAL = 50;

    /**
     * After chunk.
     *
     * @param context the context
     */
    @Override
    public void afterChunk(ChunkContext context) {
        long count = context.getStepContext().getStepExecution().getReadCount();

        if (count > 0 && count % LOGGING_INTERVAL == 0) {
            log.info("Processed {} entries", count);
        }
    }

}
