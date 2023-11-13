package com.eversis.esa.geoss.curated.resources.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * The type Source.
 */
@Getter
@Setter
@Entity
@Table(name = "source")
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column
    private String term;

    @NaturalId
    @Column(unique = true, nullable = false)
    private String code;

    @Column(name = "iscustom")
    private Integer isCustom;

    /**
     * Instantiates a new Source.
     *
     * @param code the code
     */
    public Source(String code) {
        this.code = code;
    }

    /**
     * Instantiates a new Source.
     */
    public Source() {
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
        Source source = (Source) o;
        return Objects.equals(code, source.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}
