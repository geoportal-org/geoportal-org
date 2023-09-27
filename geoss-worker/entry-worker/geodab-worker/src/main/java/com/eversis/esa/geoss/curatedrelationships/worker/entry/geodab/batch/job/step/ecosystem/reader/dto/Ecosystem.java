package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem.reader.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * The type Ecosystem.
 */
@Getter
@Setter
public class Ecosystem {

    private String id;
    @JsonProperty("image_url")
    private String imageUrl;
    private String name;
    private String description;

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
        Ecosystem ecosystem = (Ecosystem) o;
        return Objects.equals(id, ecosystem.id);
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
