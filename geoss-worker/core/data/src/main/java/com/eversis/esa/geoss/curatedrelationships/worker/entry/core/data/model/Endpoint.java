package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.util.Identifiable;

import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
@Entity
@Table(name = "endpoint")
public class Endpoint implements Identifiable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    private Integer id;

    @Column
    private String url;

    @Column(name = "urltype")
    private String urlType;

    @Column(name = "iscustom")
    private Integer isCustom = 0;

}
