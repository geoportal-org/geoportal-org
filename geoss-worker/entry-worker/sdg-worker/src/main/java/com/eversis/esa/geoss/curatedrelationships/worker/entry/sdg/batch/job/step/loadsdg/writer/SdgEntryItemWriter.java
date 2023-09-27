package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.writer;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * The type Sdg entry item writer.
 */
@Log4j2
@Component("sdgEntryItemWriter")
class SdgEntryItemWriter implements ItemWriter<Entry> {

    private final SdgEntryWriterService writerService;

    /**
     * Instantiates a new Sdg entry item writer.
     *
     * @param writerService the writer service
     */
    public SdgEntryItemWriter(SdgEntryWriterService writerService) {
        this.writerService = writerService;
    }

    /**
     * Write.
     *
     * @param chunk the chunk
     * @throws Exception the exception
     */
    @Override
    public void write(Chunk<? extends Entry> chunk) throws Exception {
        writerService.saveEntries(chunk.getItems());
    }
}
