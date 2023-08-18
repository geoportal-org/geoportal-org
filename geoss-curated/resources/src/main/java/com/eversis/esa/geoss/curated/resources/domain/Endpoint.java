package com.eversis.esa.geoss.curated.resources.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

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

    public Endpoint(String url) {
        this.url = url;
    }

    protected Endpoint() {
        // required by JPA
    }

}
