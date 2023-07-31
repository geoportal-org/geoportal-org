package com.eversis.esa.geoss.curatedrelationships.search.repository.common.exception;

/**
 * The type Result parsing exception.
 */
public class ResultParsingException extends RuntimeException {

    /**
     * Instantiates a new Result parsing exception.
     */
    public ResultParsingException() {
        super();
    }

    /**
     * Instantiates a new Result parsing exception.
     *
     * @param message the message
     */
    public ResultParsingException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Result parsing exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public ResultParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Result parsing exception.
     *
     * @param cause the cause
     */
    public ResultParsingException(Throwable cause) {
        super(cause);
    }
}
