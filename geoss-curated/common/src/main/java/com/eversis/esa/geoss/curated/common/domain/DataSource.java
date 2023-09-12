package com.eversis.esa.geoss.curated.common.domain;

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
 * The type Data source.
 */
@Getter
@Setter
@Entity
@Table(name = "datasource")
public class DataSource {

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
    public DataSource(String code) {
        this.code = code;
    }

    /**
     * Instantiates a new Data source.
     */
    protected DataSource() {
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
        DataSource that = (DataSource) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}
