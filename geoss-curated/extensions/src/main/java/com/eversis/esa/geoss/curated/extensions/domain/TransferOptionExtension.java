package com.eversis.esa.geoss.curated.extensions.domain;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.eversis.esa.geoss.curated.common.domain.Endpoint;
import com.eversis.esa.geoss.curated.common.domain.Protocol;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The type Transfer option extension.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transferoptionextension")
public class TransferOptionExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "displaytitle", nullable = true)
    private String displayTitle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entryextensionid")
    private EntryExtension entryExtension;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "endpointid")
    private Endpoint endpoint;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "protocolid")
    private Protocol protocol;

    @Column(name = "deleted", nullable = false)
    private Integer deleted = 0;

    @CreatedDate
    @Column(name = "createddate", nullable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "modifieddate", nullable = false)
    private LocalDateTime modifiedDate;

}
