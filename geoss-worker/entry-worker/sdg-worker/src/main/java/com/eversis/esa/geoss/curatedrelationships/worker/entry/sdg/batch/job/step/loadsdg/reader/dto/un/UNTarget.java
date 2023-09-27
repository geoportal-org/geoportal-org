package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.un;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Un target.
 */
@Getter
@Setter
public class UNTarget {

    private String code;
    private String title;
    private String description;
    private String uri;
    private List<UNIndicator> indicators = new ArrayList<>();

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
        UNTarget unTarget = (UNTarget) o;
        return Objects.equals(code, unTarget.code);
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
