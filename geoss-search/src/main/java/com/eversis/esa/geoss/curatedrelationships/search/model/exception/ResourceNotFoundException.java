package com.eversis.esa.geoss.curatedrelationships.search.model.exception;

/**
 * The type Resource not found exception.
 */
public class ResourceNotFoundException extends SearchFailureException {

    /**
     * Instantiates a new Resource not found exception.
     */
    public ResourceNotFoundException() {
        super();
    }

    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Resource not found exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Resource not found exception.
     *
     * @param cause the cause
     */
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
