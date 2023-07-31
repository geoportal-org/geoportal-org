package com.eversis.esa.geoss.curatedrelationships.search.model.exception;

/**
 * The type Search failure exception.
 */
public class SearchFailureException extends RuntimeException {

    /**
     * Instantiates a new Search failure exception.
     */
    public SearchFailureException() {
        super();
    }

    /**
     * Instantiates a new Search failure exception.
     *
     * @param message the message
     */
    public SearchFailureException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Search failure exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public SearchFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Search failure exception.
     *
     * @param cause the cause
     */
    public SearchFailureException(Throwable cause) {
        super(cause);
    }
}
