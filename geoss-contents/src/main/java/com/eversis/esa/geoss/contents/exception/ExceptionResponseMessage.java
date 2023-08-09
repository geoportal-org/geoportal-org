package com.eversis.esa.geoss.contents.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * The type Exception response message.
 */
@Data
@AllArgsConstructor
public class ExceptionResponseMessage {
    private String message;
    private int status;
    private Date timestamp;
    private List<String> errors;
}
