package com.eversis.esa.geoss.contents.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;

import com.eversis.esa.geoss.common.domain.AuditableEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The type Site.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Site extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "{validation.notNull}")
    @Size(min = 1, max = 255, message = "{validation.name}")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "{validation.notNull}")
    @Size(min = 1, max = 255, message = "{validation.url}")
    @Column(nullable = false)
    private String url;

    @NotNull(message = "{validation.notNull}")
    @Min(value = 0, message = "{validation.equalToOrGreaterThanZero}")
    @Column(nullable = false)
    private Long logoDocumentId;

}
