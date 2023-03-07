package com.eversis.esa.geoss.contents.exception;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolationException;

import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        body.put("message", "Validation Failed");
        List<String> errors = ex.getErrors().getAllErrors().stream()
                .map(p -> p.toString()).collect(Collectors.toList());
        body.put("errors", errors);
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
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

}
