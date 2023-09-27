package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.un;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The type Un indicator.
 */
@Getter
@Setter
public class UNIndicator {

    private String code;
    private String title;
    private String description;
    private String uri;
    private Set<UNSeries> series = new HashSet<>();

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
        UNIndicator indicator = (UNIndicator) o;
        return Objects.equals(code, indicator.code);
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
