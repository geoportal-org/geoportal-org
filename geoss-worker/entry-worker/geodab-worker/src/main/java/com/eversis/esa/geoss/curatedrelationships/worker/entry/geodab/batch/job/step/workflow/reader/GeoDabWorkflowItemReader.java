package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.service.GeoDabWorkflowLoaderService;

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
 * The type Geo dab workflow item reader.
 */
@Log4j2
@StepScope
@Component("geoDabWorkflowItemReader")
public class GeoDabWorkflowItemReader extends AbstractItemCountingItemStreamItemReader<Entry> {

    private final AbstractPagingItemReader<String> storylineIdReader;
    private final GeoDabWorkflowLoaderService geoDabWorkflowLoaderService;

    private ConcurrentLinkedQueue<Entry> workflowEntries = new ConcurrentLinkedQueue<>();

    /**
     * Instantiates a new Geo dab workflow item reader.
     *
     * @param storylineIdReader the storyline id reader
     * @param geoDabWorkflowLoaderService the geo dab workflow loader service
     */
    @Autowired
    public GeoDabWorkflowItemReader(
            @Qualifier("storylineIdItemReader") AbstractPagingItemReader<String> storylineIdReader,
            GeoDabWorkflowLoaderService geoDabWorkflowLoaderService) {
        this.storylineIdReader = storylineIdReader;
        this.geoDabWorkflowLoaderService = geoDabWorkflowLoaderService;
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
        storylineIdReader.open(executionContext);
    }

    @Override
    protected Entry doRead() throws Exception {
        Entry workflowEntry = readItem();
        if (workflowEntry != null) {
            return workflowEntry;
        }
        fetchWorkflowEntries();
        return readItem();
    }

    private Entry readItem() {
        return workflowEntries.poll();
    }

    @Override
    protected void doOpen() throws Exception {
        fetchWorkflowEntries();
    }

    protected void fetchWorkflowEntries() throws Exception {
        String currentStorylineId = storylineIdReader.read();
        List<Entry> currentlyProcessedWorkflows = new ArrayList<>();

        while (CollectionUtils.isEmpty(currentlyProcessedWorkflows) && StringUtils.hasLength(currentStorylineId)) {
            currentlyProcessedWorkflows = geoDabWorkflowLoaderService.fetchEntries(currentStorylineId);
            if (CollectionUtils.isEmpty(currentlyProcessedWorkflows)) {
                currentStorylineId = storylineIdReader.read();
            }
        }
        workflowEntries.addAll(currentlyProcessedWorkflows);
    }

    @Override
    protected void doClose() throws Exception {
        // Do nothing
    }

}
