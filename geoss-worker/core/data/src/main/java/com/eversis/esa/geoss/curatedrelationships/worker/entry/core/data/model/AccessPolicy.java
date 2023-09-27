package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * The type Access policy.
 */
@Getter
@Setter
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Entity
@Table(name = "accesspolicy")
public class AccessPolicy {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer id;

    @NaturalId
    @Column(unique = true, nullable = false)
    private String code;

    @Column
    private String name;

    @Column(name = "iscustom")
    private Integer isCustom = 0;

    /**
     * Instantiates a new Access policy.
     *
     * @param code the code
     */
    public AccessPolicy(String code) {
        this.code = code;
    }

    protected AccessPolicy() {
        // required by JPA
    }

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
        AccessPolicy that = (AccessPolicy) o;
        return Objects.equals(code, that.code);
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}
