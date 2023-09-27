package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.writer;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.EntryService;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * The type Storyline item writer.
 */
@Log4j2
@Component("storylineItemWriter")
class StorylineItemWriter implements ItemWriter<Entry> {

    private final EntryService entryService;

    /**
     * Instantiates a new Storyline item writer.
     *
     * @param entryService the entry service
     */
    public StorylineItemWriter(EntryService entryService) {
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
