package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.reader.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Protected area response.
 */
@Getter
@Setter
public class ProtectedAreaResponse {

    @JsonProperty("pas")
    private List<ProtectedArea> protectedAreas = new ArrayList<>();

}
