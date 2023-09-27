package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.service.SdgEntryLoaderService;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The type Sdg entry item reader.
 */
@Log4j2
@StepScope
@Component("sdgEntryItemReader")
class SdgEntryItemReader implements ItemReader<Entry> {

    private final SdgEntryLoaderService loaderService;
    private ConcurrentLinkedQueue<Entry> entries;

    /**
     * Instantiates a new Sdg entry item reader.
     *
     * @param loaderService the loader service
     */
    public SdgEntryItemReader(final SdgEntryLoaderService loaderService) {
        this.loaderService = loaderService;
    }

    /**
     * Read entry.
     *
     * @return the entry
     * @throws Exception the exception
     * @throws UnexpectedInputException the unexpected input exception
     * @throws ParseException the parse exception
     * @throws NonTransientResourceException the non transient resource exception
     */
    @Override
    public Entry read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!isEntryListInitialised()) {
            entries = new ConcurrentLinkedQueue<>();
            fetchEntries();
        }

        return entries.poll();
    }

    private boolean isEntryListInitialised() {
        return entries != null;
    }

    private void fetchEntries() {
        entries.addAll(loaderService.fetchEntries());
    }

}
