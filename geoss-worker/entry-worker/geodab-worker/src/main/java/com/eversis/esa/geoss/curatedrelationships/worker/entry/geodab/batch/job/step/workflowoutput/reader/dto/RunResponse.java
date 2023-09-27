package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Run response.
 */
@Data
public class RunResponse {

    private List<Run> runs = new ArrayList<>();
    private int total = 0;

}
