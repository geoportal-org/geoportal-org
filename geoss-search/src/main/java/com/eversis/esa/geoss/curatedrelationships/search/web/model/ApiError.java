package com.eversis.esa.geoss.curatedrelationships.search.web.model;

import java.time.LocalDateTime;

public class ApiError {

    private LocalDateTime dateTime;
    private String message;
    private int status;

    public ApiError(LocalDateTime dateTime, String message, int status) {
        this.dateTime = dateTime;
        this.message = message;
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
