package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto.Run;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.service.WorkflowOutputLoaderService;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The type Workflow output item reader.
 */
@Log4j2
@StepScope
@Component("workflowOutputItemReader")
public class WorkflowOutputItemReader extends AbstractItemCountingItemStreamItemReader<Entry> {

    private final WorkflowRunItemReader runItemReader;
    private final WorkflowOutputLoaderService workflowOutputLoaderService;

    private ConcurrentLinkedQueue<Entry> workflowOutputEntries = new ConcurrentLinkedQueue<>();

    /**
     * Instantiates a new Workflow output item reader.
     *
     * @param runItemReader the run item reader
     * @param workflowOutputLoaderService the workflow output loader service
     */
    @Autowired
    public WorkflowOutputItemReader(
            WorkflowRunItemReader runItemReader,
            WorkflowOutputLoaderService workflowOutputLoaderService) {
        this.runItemReader = runItemReader;
        this.workflowOutputLoaderService = workflowOutputLoaderService;
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
        runItemReader.open(executionContext);
    }

    @Override
    protected Entry doRead() throws Exception {
        Entry workflowOutputEntry = readItem();
        if (workflowOutputEntry != null) {
            return workflowOutputEntry;
        }
        fetchWorkflowOutputEntries();
        return readItem();
    }

    private Entry readItem() {
        return workflowOutputEntries.poll();
    }

    @Override
    protected void doOpen() throws Exception {
        fetchWorkflowOutputEntries();
    }

    protected void fetchWorkflowOutputEntries() throws Exception {
        Run currentRun = runItemReader.read();
        List<Entry> currentlyProcessedWorkflowOutputs = new ArrayList<>();

        while (CollectionUtils.isEmpty(currentlyProcessedWorkflowOutputs) && currentRun != null) {
            currentlyProcessedWorkflowOutputs = workflowOutputLoaderService.fetchEntries(currentRun);
            if (CollectionUtils.isEmpty(currentlyProcessedWorkflowOutputs)) {
                currentRun = runItemReader.read();
            }
        }
        workflowOutputEntries.addAll(currentlyProcessedWorkflowOutputs);
    }

    @Override
    protected void doClose() throws Exception {
        // Do nothing
    }

}
