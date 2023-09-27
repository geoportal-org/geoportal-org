package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.geoportal;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * The type Geoss sdg node.
 */
@Getter
@Setter
public class GeossSDGNode {

    private String number;
    private String title;
    private String description;
    private long modifiedDate;

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
        GeossSDGNode that = (GeossSDGNode) o;
        return Objects.equals(number, that.number);
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
