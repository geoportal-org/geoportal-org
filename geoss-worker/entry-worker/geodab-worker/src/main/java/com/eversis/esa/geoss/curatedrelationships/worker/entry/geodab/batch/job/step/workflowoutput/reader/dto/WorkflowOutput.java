package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto;

import lombok.Data;

import java.util.Objects;

/**
 * The type Workflow output.
 */
@Data
public class WorkflowOutput {

    private String id;
    private String name;
    private String description;
    private Object value;
    private Object valueArray;
    private String outputType;
    private String valueSchema;
    private boolean createFolder;
    private String target;

    /**
     * Equals boolean.
     *
     * @param o the o
     * @return the boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkflowOutput workflowOutput = (WorkflowOutput) o;
        return Objects.equals(id, workflowOutput.id);
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
