package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem.reader.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Ecosystems response.
 */
@Getter
@Setter
public class EcosystemsResponse {

    private List<Ecosystem> ecosystems = new ArrayList<>();

}
