package com.eversis.esa.geoss.curatedrelationships.search.web.model;

import java.time.LocalDateTime;

/**
 * The type Api error.
 */
public class ApiError {

    private LocalDateTime dateTime;
    private String message;
    private int status;

    /**
     * Instantiates a new Api error.
     *
     * @param dateTime the date time
     * @param message the message
     * @param status the status
     */
    public ApiError(LocalDateTime dateTime, String message, int status) {
        this.dateTime = dateTime;
        this.message = message;
        this.status = status;
    }

    /**
     * Gets date time.
     *
     * @return the date time
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public int getStatus() {
        return status;
    }
}
