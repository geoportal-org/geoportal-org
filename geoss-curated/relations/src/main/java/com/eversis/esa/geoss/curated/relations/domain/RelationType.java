package com.eversis.esa.geoss.curated.relations.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * The type Relation type.
 */
@Getter
@Setter
@Entity
@Table(name = "relationtype")
public class RelationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NaturalId
    @Column(unique = true, nullable = false)
    private String code;

    @Column
    private String name;

    /**
     * Instantiates a new Relation type.
     *
     * @param code the code
     */
    public RelationType(String code) {
        this.code = code;
    }

    /**
     * Instantiates a new Relation type.
     */
    protected RelationType() {
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
        RelationType that = (RelationType) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}
