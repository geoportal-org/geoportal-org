package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * The type Thesaurus job model.
 */
@Data
public class ThesaurusJobModel {

    private final ThesaurusType type;

    private volatile Status status = Status.UNKNOWN;

    private volatile LocalDateTime startTime = null;

    private volatile LocalDateTime createTime = LocalDateTime.now();

    private volatile LocalDateTime endTime = null;

    private volatile LocalDateTime lastUpdated = null;

    @JsonIgnore
    private transient volatile Throwable throwable = null;

    /**
     * Gets cause.
     *
     * @return the cause
     */
    @JsonProperty("cause")
    public ExceptionMessage getCause() {
        return throwable != null ? new ExceptionMessage(throwable) : null;
    }

    /**
     * Is pending boolean.
     *
     * @return the boolean
     */
    @JsonIgnore
    public boolean isPending() {
        return status.isRunning();
    }
}
