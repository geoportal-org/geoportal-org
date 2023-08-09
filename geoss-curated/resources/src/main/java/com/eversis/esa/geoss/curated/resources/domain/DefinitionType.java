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
 * The type Definition type.
 */
@Getter
@Setter
@Entity
@Table(name = "definitiontype")
public class DefinitionType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NaturalId
    @Column(unique = true, nullable = false)
    private int code;

    @Column
    private String name;

    /**
     * Instantiates a new Definition type.
     *
     * @param code the code
     */
    public DefinitionType(int code) {
        this.code = code;
    }

    /**
     * Instantiates a new Definition type.
     */
    protected DefinitionType() {
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
        DefinitionType that = (DefinitionType) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}
