package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.service.VlabDabWorkflowLoaderService;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * The type Vlab dab workflow item reader.
 */
@Log4j2
@StepScope
@Component("vlabDabWorkflowItemReader")
public class VlabDabWorkflowItemReader extends AbstractPaginatedDataItemReader<Entry> {

    private final VlabDabWorkflowLoaderService vlabDabWorkflowLoaderService;

    /**
     * Instantiates a new Vlab dab workflow item reader.
     *
     * @param vlabDabWorkflowLoaderService the vlab dab workflow loader service
     */
    @Autowired
    public VlabDabWorkflowItemReader(VlabDabWorkflowLoaderService vlabDabWorkflowLoaderService) {
        this.vlabDabWorkflowLoaderService = vlabDabWorkflowLoaderService;
        setSaveState(false);
        setPageSize(5);
    }

    @Override
    protected Iterator<Entry> doPageRead() {
        return vlabDabWorkflowLoaderService.fetchEntries(page * pageSize, pageSize).iterator();
    }

}
