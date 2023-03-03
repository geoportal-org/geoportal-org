package com.eversis.esa.geoss.settings.system.domain;

import com.eversis.esa.geoss.settings.common.domain.AuditableEmbeddable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

/**
 * The type Web settings.
 */
@Data
@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
public class WebSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "set_", nullable = false)
    private WebSettingsSet set;

    @Schema(enumAsRef = true, allowableValues = "{}")
    @NotNull
    @Column(name = "key_", nullable = false)
    private String key;

    @Column(name = "value_")
    private String value;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonUnwrapped
    @NotAudited
    @Embedded
    private AuditableEmbeddable auditable = new AuditableEmbeddable();

    /**
     * Sets key.
     *
     * @param key the key
     */
    @JsonSetter
    public void setKey(String key) {
        this.set.validKey(key);
        this.key = key;
    }
}
