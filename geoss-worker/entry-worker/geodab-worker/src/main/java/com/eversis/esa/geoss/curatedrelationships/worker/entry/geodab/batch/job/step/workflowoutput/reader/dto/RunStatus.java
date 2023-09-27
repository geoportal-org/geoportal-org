package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Run status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RunStatus {

    /**
     * The constant SUCCESSFULLY_COMPLETED.
     */
    public static final RunStatus SUCCESSFULLY_COMPLETED = new RunStatus("SUCCESS", "COMPLETED");

    private String result;
    private String status;

}
