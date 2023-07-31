package com.eversis.esa.geoss.curatedrelationships.search.model.exception;

public class SearchFailureException extends RuntimeException{

    public SearchFailureException() {
        super();
    }

    public SearchFailureException(String message) {
        super(message);
    }

    public SearchFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchFailureException(Throwable cause) {
        super(cause);
    }
}
