package com.eversis.esa.geoss.curated.recommendations.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type Recommendation.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "recommendation")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    private Long id;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID uuid = UUID.randomUUID();

    @CreatedDate
    @Column(name = "createddate", nullable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "modifieddate", nullable = false)
    private LocalDateTime modifiedDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "recommendation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RecommendedEntity> entities = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "recommendation",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RecommendedKeyword> keywords = new ArrayList<>();
}
