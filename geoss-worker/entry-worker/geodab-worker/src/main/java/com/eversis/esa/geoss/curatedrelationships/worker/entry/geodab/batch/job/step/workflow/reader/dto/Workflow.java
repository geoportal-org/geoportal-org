package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Objects;

/**
 * The type Workflow.
 */
@Data
public class Workflow {

    private String id;
    private Boolean underTest;
    @JsonProperty("bpmn_url")
    private String bpmnUrl;
    private String name;
    private String description;
    private String modelDeveloperOrg;
    private String modelDeveloper;
    private String modelDeveloperEmail;

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
        Workflow workflow = (Workflow) o;
        return Objects.equals(id, workflow.id);
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
