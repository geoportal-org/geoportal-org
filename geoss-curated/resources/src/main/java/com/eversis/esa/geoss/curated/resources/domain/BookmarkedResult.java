package com.eversis.esa.geoss.curated.resources.domain;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The type Bookmarked result.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "bookmarked_result")
public class BookmarkedResult {

    @Id
    @Column(name = "id_")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "targetid", nullable = true)
    private String targetId;

    @Column(name = "entryxml", nullable = true)
    private String entryXml;

    @Column(name = "userid", nullable = true)
    private String userId;

    @Column(name = "currmap", nullable = true)
    private String currMap;

    @Column(name = "groupid", nullable = true)
    private Long groupId;

    @Column(name = "datasource",  nullable = true)
    private String dataSource;

    @Column(name = "sourcebaseurl", nullable = true)
    private String sourceBaseUrl;

    @Column(name = "valid", nullable = true)
    private Integer valid;

    @CreatedDate
    @Column(name = "savedate", nullable = false)
    private LocalDateTime saveDate;

    @LastModifiedDate
    @Column(name = "modifieddate", nullable = false)
    private LocalDateTime modifiedDate;

}
