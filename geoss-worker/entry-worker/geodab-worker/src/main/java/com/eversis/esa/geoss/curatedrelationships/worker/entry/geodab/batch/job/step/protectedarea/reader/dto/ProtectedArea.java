package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.reader.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * The type Protected area.
 */
@Getter
@Setter
public class ProtectedArea {

    private String id;
    @JsonProperty("kml_url")
    private String kmlUrl;
    @JsonProperty("image_url")
    private String imageUrl;
    private String name;
    private String lon;
    private String lat;

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
        ProtectedArea protectedArea = (ProtectedArea) o;
        return Objects.equals(id, protectedArea.id);
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
