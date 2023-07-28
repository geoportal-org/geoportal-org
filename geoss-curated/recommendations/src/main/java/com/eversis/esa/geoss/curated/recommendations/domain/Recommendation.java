package com.eversis.esa.geoss.curated.recommendations.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    @JsonProperty(access = Access.READ_ONLY)
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

    /**
     * Add recommended entity.
     *
     * @param recommendedEntity the recommended entity
     */
    public void addRecommendedEntity(RecommendedEntity recommendedEntity) {
        entities.add(recommendedEntity);
        recommendedEntity.setRecommendation(this);
    }

    /**
     * Remove recommended entity.
     *
     * @param recommendedEntity the recommended entity
     */
    public void removeRecommendedEntity(RecommendedEntity recommendedEntity) {
        entities.remove(recommendedEntity);
        recommendedEntity.setRecommendation(null);
    }

    /**
     * Add recommended keyword.
     *
     * @param recommendedKeyword the recommended keyword
     */
    public void addRecommendedKeyword(RecommendedKeyword recommendedKeyword) {
        keywords.add(recommendedKeyword);
        recommendedKeyword.setRecommendation(this);
    }

    /**
     * Remove recommended keyword.
     *
     * @param recommendedKeyword the recommended keyword
     */
    public void removeRecommendedKeyword(RecommendedKeyword recommendedKeyword) {
        keywords.remove(recommendedKeyword);
        recommendedKeyword.setRecommendation(null);
    }

    /**
     * Clear recommended keywords.
     */
    public void clearRecommendedKeywords() {
        keywords.forEach(recommendedKeyword -> recommendedKeyword.setRecommendation(null));
        keywords.clear();
    }

    /**
     * Sets entities.
     *
     * @param entities the entities
     */
    public void setEntities(List<RecommendedEntity> entities) {
        this.entities = Optional.ofNullable(entities).orElse(Collections.emptyList());
        this.entities.forEach(recommendedEntity -> recommendedEntity.setRecommendation(this));
    }

    /**
     * Sets keywords.
     *
     * @param keywords the keywords
     */
    public void setKeywords(List<RecommendedKeyword> keywords) {
        this.keywords = Optional.ofNullable(keywords).orElse(Collections.emptyList());
        this.keywords.forEach(recommendedKeyword -> recommendedKeyword.setRecommendation(this));
    }
}
