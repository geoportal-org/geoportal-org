package com.eversis.esa.geoss.curated.resources.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * The type User resource.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_resource")
public class UserResource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "userid", nullable = false)
    private String userId;

    @Column(name = "entryname", nullable = false)
    private String entryName;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tasktype", nullable = false)
    private TaskType taskType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entryid")
    private Entry entry;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private Status status;

    @CreatedDate
    @Column(name = "createddate", nullable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "modifieddate", nullable = false)
    private LocalDateTime modifiedDate;

}
