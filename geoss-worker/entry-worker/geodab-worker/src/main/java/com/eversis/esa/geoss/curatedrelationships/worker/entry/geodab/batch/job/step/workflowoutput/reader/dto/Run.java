package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The type Run.
 */
@Data
public class Run {

    @JsonProperty("runid")
    private String runId;

    @JsonProperty("workflowid")
    private String workflowId;

}
