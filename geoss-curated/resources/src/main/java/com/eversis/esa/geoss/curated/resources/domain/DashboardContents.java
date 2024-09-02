package com.eversis.esa.geoss.curated.resources.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * The type Dashboard contents.
 */
@Getter
@Setter
@Entity
@Table(name = "dashboardcontents")
public class DashboardContents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT", nullable = true)
    private String content;

    /**
     * Instantiates a new Dashboard contents.
     *
     * @param content the content
     */
    public DashboardContents(String content) {
        this.content = content;
    }

    /**
     * Instantiates a new Dashboard contents.
     */
    public DashboardContents() {
        // required by JPA
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DashboardContents that = (DashboardContents) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

}
