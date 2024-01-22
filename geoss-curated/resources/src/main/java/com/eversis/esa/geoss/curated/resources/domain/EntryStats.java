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
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The type Entry stats.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "entry_stats")
public class EntryStats {

    @Id
    @Column(name = "statsid")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long statsId;

    @Column(name = "targetid", nullable = true)
    private String targetId;

    @Column(name = "totalentries", nullable = true)
    private Integer totalEntries;

    @Column(name = "totalscore", nullable = true)
    private Double totalScore;

    @Column(name = "averagescore", nullable = true)
    private Double averageScore;

    @Column(name = "datasource",  nullable = true)
    private String dataSource;

    @LastModifiedDate
    @Column(name = "modifieddate", nullable = false)
    private LocalDateTime modifiedDate;

}
