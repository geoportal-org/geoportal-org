package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.reader;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.reader.service.StorylineLoaderService;

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
 * The type Storyline item reader.
 */
@Log4j2
@StepScope
@Component("storylineItemReader")
public class StorylineItemReader extends AbstractItemCountingItemStreamItemReader<Entry> {

    private final AbstractPagingItemReader<String> protectedAreaIdReader;
    private final StorylineLoaderService storylineLoaderService;

    private ConcurrentLinkedQueue<Entry> storylineEntries = new ConcurrentLinkedQueue<>();

    /**
     * Instantiates a new Storyline item reader.
     *
     * @param protectedAreaIdReader the protected area id reader
     * @param storylineLoaderService the storyline loader service
     */
    @Autowired
    public StorylineItemReader(
            @Qualifier("protectedAreaIdItemReader") AbstractPagingItemReader<String> protectedAreaIdReader,
            StorylineLoaderService storylineLoaderService) {
        this.protectedAreaIdReader = protectedAreaIdReader;
        this.storylineLoaderService = storylineLoaderService;
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
        protectedAreaIdReader.open(executionContext);
    }

    @Override
    protected Entry doRead() throws Exception {
        Entry protectedAreaEntry = readItem();
        if (protectedAreaEntry != null) {
            return protectedAreaEntry;
        }
        fetchStorylineEntries();
        return readItem();
    }

    private Entry readItem() {
        return storylineEntries.poll();
    }

    @Override
    protected void doOpen() throws Exception {
        fetchStorylineEntries();
    }

    protected void fetchStorylineEntries() throws Exception {
        String currentProtectedAreaId = protectedAreaIdReader.read();
        List<Entry> currentlyProcessedStorylines = new ArrayList<>();

        while (CollectionUtils.isEmpty(currentlyProcessedStorylines) && StringUtils.hasLength(currentProtectedAreaId)) {
            currentlyProcessedStorylines = storylineLoaderService.fetchEntries(currentProtectedAreaId);
            if (CollectionUtils.isEmpty(currentlyProcessedStorylines)) {
                currentProtectedAreaId = protectedAreaIdReader.read();
            }
        }
        storylineEntries.addAll(currentlyProcessedStorylines);
    }

    @Override
    protected void doClose() throws Exception {
        // Do nothing
    }

}
