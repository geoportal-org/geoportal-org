package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.writer;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.EntryService;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Sdg entry writer service.
 */
@Log4j2
@Service
class SdgEntryWriterService {

    private EntryService entryService;

    /**
     * Instantiates a new Sdg entry writer service.
     *
     * @param entryService the entry service
     */
    public SdgEntryWriterService(EntryService entryService) {
        this.entryService = entryService;
    }

    /**
     * Save entries.
     *
     * @param items the items
     */
    @Transactional
    public void saveEntries(List<? extends Entry> items) {
        entryService.saveEntries(items);
    }
}
