package com.eversis.esa.geoss.curated.extensions.domain;

import com.eversis.esa.geoss.curated.common.domain.DataSource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
import java.time.LocalDateTime;

/**
 * The type Entry extension.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "entryextension")
public class EntryExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NaturalId
    @Column(name = "code", nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "datasourceid", nullable = false)
    private DataSource dataSource;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "summary", columnDefinition = "TEXT", nullable = true)
    private String summary;

    @Column(name = "keywords", nullable = true)
    private String keywords;

    @Column(name = "tags", nullable = true)
    private String tags;

    @Column(name = "comment",columnDefinition = "TEXT", nullable = true)
    private String comment;

    @Column(name = "workflowinstanceid", nullable = true)
    private Long workflowInstanceId;

    @Column(name = "userid", nullable = false)
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "deleted", nullable = false)
    private Integer deleted = 0;

    @CreatedDate
    @Column(name = "createddate", nullable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "modifieddate", nullable = false)
    private LocalDateTime modifiedDate;

}
