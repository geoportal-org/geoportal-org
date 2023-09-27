package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.reader;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.reader.service.ProtectedAreaLoaderService;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The type Protected area item reader.
 */
@Log4j2
@StepScope
@Component("protectedAreaItemReader")
public class ProtectedAreaItemReader extends AbstractItemCountingItemStreamItemReader<Entry> {

    private final AbstractPagingItemReader<String> ecosystemIdReader;
    private final ProtectedAreaLoaderService protectedAreaLoaderService;

    private ConcurrentLinkedQueue<Entry> protectedAreaEntries = new ConcurrentLinkedQueue<>();

    /**
     * Instantiates a new Protected area item reader.
     *
     * @param ecosystemIdReader the ecosystem id reader
     * @param protectedAreaLoaderService the protected area loader service
     */
    @Autowired
    public ProtectedAreaItemReader(
            @Qualifier("ecosystemIdItemReader") AbstractPagingItemReader<String> ecosystemIdReader,
            ProtectedAreaLoaderService protectedAreaLoaderService) {
        this.ecosystemIdReader = ecosystemIdReader;
        this.protectedAreaLoaderService = protectedAreaLoaderService;
        setSaveState(false);
    }

    /**
     * Open.
     *
     * @param executionContext the execution context
     * @throws ItemStreamException the item stream exception
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        super.open(executionContext);
        ecosystemIdReader.open(executionContext);
    }

    @Override
    protected Entry doRead() throws Exception {
        Entry protectedAreaEntry = readItem();
        if (protectedAreaEntry != null) {
            return protectedAreaEntry;
        }
        fetchProtectedAreasEntries();
        return readItem();
    }

    private Entry readItem() {
        return protectedAreaEntries.poll();
    }

    @Override
    protected void doOpen() throws Exception {
        fetchProtectedAreasEntries();
    }

    protected void fetchProtectedAreasEntries() throws Exception {
        String currentEcosystemId = ecosystemIdReader.read();
        List<Entry> currentlyProcessedProtectedAreas = new ArrayList<>();

        while (CollectionUtils.isEmpty(currentlyProcessedProtectedAreas) && StringUtils.hasLength(currentEcosystemId)) {
            currentlyProcessedProtectedAreas = protectedAreaLoaderService.fetchEntries(currentEcosystemId);
            if (CollectionUtils.isEmpty(currentlyProcessedProtectedAreas)) {
                currentEcosystemId = ecosystemIdReader.read();
            }
        }
        protectedAreaEntries.addAll(currentlyProcessedProtectedAreas);
    }

    @Override
    protected void doClose() throws Exception {
        // Do nothing
    }

}
