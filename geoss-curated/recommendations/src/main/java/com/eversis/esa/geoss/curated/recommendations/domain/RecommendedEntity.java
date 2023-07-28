package com.eversis.esa.geoss.curated.recommendations.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Types;
import java.util.UUID;

/**
 * The type Recommended entity.
 */
@Data
@Entity
@Table(name = "recommendation_entity")
public class RecommendedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    @JdbcTypeCode(Types.VARCHAR)
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "orderweight", nullable = false)
    private double orderWeight = 1.0;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "datasourceid")
    private DataSource dataSource;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendationid")
    private Recommendation recommendation;
}
