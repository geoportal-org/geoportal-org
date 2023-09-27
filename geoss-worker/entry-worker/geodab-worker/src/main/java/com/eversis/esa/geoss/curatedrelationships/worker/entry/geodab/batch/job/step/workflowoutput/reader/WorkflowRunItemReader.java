package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto.Run;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.service.RunLoaderService;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * The type Workflow run item reader.
 */
@Log4j2
@StepScope
@Component("workflowRunItemReader")
class WorkflowRunItemReader extends AbstractPaginatedDataItemReader<Run> {

    private final RunLoaderService runLoaderService;

    /**
     * Instantiates a new Workflow run item reader.
     *
     * @param runLoaderService the run loader service
     */
    @Autowired
    public WorkflowRunItemReader(RunLoaderService runLoaderService) {
        this.runLoaderService = runLoaderService;
        setSaveState(false);
        setPageSize(5);
    }

    @Override
    protected Iterator<Run> doPageRead() {
        return runLoaderService.fetchSuccessfullyCompletedRunEntries(page * pageSize, pageSize).iterator();
    }

}
