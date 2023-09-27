package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.un;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

/**
 * The type Un series.
 */
@Getter
@Setter
public class UNSeries {

    private String release;
    private String code;
    private String description;
    private String uri;

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
        UNSeries series = (UNSeries) o;
        return Objects.equals(code, series.code);
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(code)
                .toHashCode();
    }

}
