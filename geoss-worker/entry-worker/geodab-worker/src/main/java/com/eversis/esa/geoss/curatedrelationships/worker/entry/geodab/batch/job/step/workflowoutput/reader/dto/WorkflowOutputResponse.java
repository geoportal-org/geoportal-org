package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Workflow output response.
 */
@Data
public class WorkflowOutputResponse {

    private List<WorkflowOutput> outputs = new ArrayList<>();

}
