package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem.reader;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem.reader.service.EcosystemLoaderService;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The type Ecosystem item reader.
 */
@Log4j2
@StepScope
@Component("ecosystemItemReader")
class EcosystemItemReader implements ItemReader<Entry> {

    private final EcosystemLoaderService loaderService;
    private ConcurrentLinkedQueue<Entry> entries;

    /**
     * Instantiates a new Ecosystem item reader.
     *
     * @param loaderService the loader service
     */
    public EcosystemItemReader(final EcosystemLoaderService loaderService) {
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
