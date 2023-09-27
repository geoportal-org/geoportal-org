package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.reader.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Storyline response.
 */
@Getter
@Setter
public class StorylineResponse {

    private List<Storyline> storylines = new ArrayList<>();

}
