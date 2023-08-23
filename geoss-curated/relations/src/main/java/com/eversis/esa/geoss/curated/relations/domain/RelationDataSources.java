package com.eversis.esa.geoss.curated.relations.domain;

import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

/**
 * The type Data source.
 */
@Getter
@Setter
@Entity
@Table(name = "datasource")
public class RelationDataSources {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column
    private String name;

    @NaturalId
    @Column(unique = true, nullable = false)
    private String code;

    @Column(name = "iscustom")
    private Integer isCustom;

    /**
     * Instantiates a new Data source.
     *
     * @param code the code
     */
    public RelationDataSources(String code) {
        this.code = code;
    }

    /**
     * Instantiates a new Data source.
     */
    protected RelationDataSources() {
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
        RelationDataSources that = (RelationDataSources) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}
