package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Workflow response.
 */
@Data
public class WorkflowResponse {

    private List<Workflow> workflows = new ArrayList<>();

}
