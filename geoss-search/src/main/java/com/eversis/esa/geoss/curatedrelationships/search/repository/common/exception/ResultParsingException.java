package com.eversis.esa.geoss.curatedrelationships.search.repository.common.exception;

public class ResultParsingException extends RuntimeException {

    public ResultParsingException() {
        super();
    }

    public ResultParsingException(String message) {
        super(message);
    }

    public ResultParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultParsingException(Throwable cause) {
        super(cause);
    }
}
