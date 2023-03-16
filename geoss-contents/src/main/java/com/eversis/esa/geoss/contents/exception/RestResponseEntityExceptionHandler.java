package com.eversis.esa.geoss.contents.exception;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolationException;

import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The type Rest response entity exception handler.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle repository constraint violation response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler({RepositoryConstraintViolationException.class})
    public ResponseEntity<Object> handleRepositoryConstraintViolation(final RepositoryConstraintViolationException ex) {
        List<String> errors = ex.getErrors().getAllErrors().stream()
                .map(p -> p.toString()).collect(Collectors.toList());
        ExceptionResponseMessage responseMessage = new ExceptionResponseMessage("Validation Failed.",
                HttpStatus.UNPROCESSABLE_ENTITY.value(), new Date(), errors);
        return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handle constraint violation response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex) {
        return new ResponseEntity<>(new ConstraintViolationExceptionMessage(ex), new HttpHeaders(),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handle max size exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ExceptionResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        List<String> errors = Arrays.asList(new String[] {ex.getMessage()});
        ExceptionResponseMessage responseMessage =
                new ExceptionResponseMessage("File is too large. Max file size is 10MB.",
                        HttpStatus.EXPECTATION_FAILED.value(), new Date(), errors);
        return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
    }
}
