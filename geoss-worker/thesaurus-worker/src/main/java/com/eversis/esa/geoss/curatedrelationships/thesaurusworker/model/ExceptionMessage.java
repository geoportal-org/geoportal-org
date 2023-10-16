package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

/**
 * The type Exception message.
 */
@RequiredArgsConstructor
public class ExceptionMessage {

    private final Throwable throwable;

    /**
     * Gets message.
     *
     * @return the message
     */
    @JsonProperty("message")
    public String getMessage() {
        return throwable.getMessage();
    }

    /**
     * Gets cause.
     *
     * @return the cause
     */
    @JsonProperty("cause")
    public ExceptionMessage getCause() {
        return throwable.getCause() != null ? new ExceptionMessage(throwable.getCause()) : null;
    }

    /**
     * Suppressed list.
     *
     * @return the list
     */
    @JsonProperty("suppressed")
    public List<ExceptionMessage> getSuppressed() {
        return Stream.of(throwable.getSuppressed())
                .map(ExceptionMessage::new)
                .toList();
    }
}
