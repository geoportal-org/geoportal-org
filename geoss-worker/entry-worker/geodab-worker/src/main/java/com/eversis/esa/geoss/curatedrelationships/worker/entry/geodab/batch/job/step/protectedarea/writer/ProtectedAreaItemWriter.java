package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.writer;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.EntryService;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * The type Protected area item writer.
 */
@Log4j2
@Component("protectedAreaItemWriter")
class ProtectedAreaItemWriter implements ItemWriter<Entry> {

    private final EntryService entryService;

    /**
     * Instantiates a new Protected area item writer.
     *
     * @param entryService the entry service
     */
    public ProtectedAreaItemWriter(EntryService entryService) {
        this.entryService = entryService;
    }

    /**
     * Write.
     *
     * @param chunk the chunk
     * @throws Exception the exception
     */
    @Override
    public void write(Chunk<? extends Entry> chunk) throws Exception {
        entryService.saveEntries(chunk.getItems());
    }
}
