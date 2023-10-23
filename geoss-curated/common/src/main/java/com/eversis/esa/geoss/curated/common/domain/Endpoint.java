package com.eversis.esa.geoss.curated.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The type Endpoint.
 */
@Data
@Entity
@Table(name = "endpoint")
public class Endpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "url", nullable = true)
    private String url;

    @Column(name = "urltype", nullable = true)
    private String urlType;

    @Column(name = "iscustom", nullable = false)
    private Integer isCustom = 0;

    /**
     * Instantiates a new Endpoint.
     */
    public Endpoint(String url) {
        this.url = url;
    }

    /**
     * Instantiates a new Endpoint.
     */
    protected Endpoint() {
        // required by JPA
    }

}
