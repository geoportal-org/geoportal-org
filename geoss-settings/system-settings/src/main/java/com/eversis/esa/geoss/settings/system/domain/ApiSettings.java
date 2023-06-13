package com.eversis.esa.geoss.settings.system.domain;

import com.eversis.esa.geoss.common.domain.AuditableEmbeddable;
import com.eversis.esa.geoss.settings.system.support.ApiSettingsKeyAttributeConverter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

/**
 * The type Api settings.
 */
@Data
@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ApiSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonIgnore
    @Version
    @Column
    private Long version;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "set_", nullable = false)
    private ApiSettingsSet set;

    @NotNull
    @Convert(converter = ApiSettingsKeyAttributeConverter.class)
    @Column(name = "key_", nullable = false)
    private ApiSettingsKey key;

    @Column(name = "value_")
    private String value;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
        this.key = this.set.getKey(key);
    }
}
