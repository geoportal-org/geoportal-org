package com.eversis.esa.geoss.curated.resources.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * The type Entry event.
 */
@Data
@Entity
public class EntryEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    @Column(nullable = false, name = "timestamp_")
    private Instant timestamp = Instant.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "type_")
    private EntryEventType type;

    @NotNull
    @Column(nullable = false)
    private Long entryId;
}
