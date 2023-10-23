package com.eversis.esa.geoss.curated.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The type Protocol.
 */
@Data
@Entity
@Table(name = "protocol")
public class Protocol {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NaturalId
    @Column(unique = true, nullable = true)
    private String value;

    @Column(name = "iscustom", nullable = false)
    private Integer isCustom = 0;

    /**
     * Instantiates a new Protocol.
     *
     * @param value the value
     */
    public Protocol(String value) {
        this.value = value;
    }

    /**
     * Instantiates a new Protocol.
     */
    protected Protocol() {
        // required by JPA
    }

}
