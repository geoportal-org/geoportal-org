package com.eversis.esa.geoss.settings.application.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.ConstraintViolationException;

/**
 * The type Rest exception handler.
 */
@Log4j2
@ControllerAdvice(
        annotations = {
                RestController.class,
                BasePathAwareController.class,
                RepositoryRestController.class,
                RepositoryRestResource.class
        })
public class RestExceptionHandler extends RepositoryRestExceptionHandler {

    /**
     * Instantiates a new Rest exception handler.
     *
     * @param messageSource the message source
     */
    public RestExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Handle repository constraint violation exception response entity.
     *
     * @param e the e
     * @return the response entity
     */
    @ExceptionHandler
    ResponseEntity<ConstraintViolationExceptionMessage> handleRepositoryConstraintViolationException(
            ConstraintViolationException e) {
        return response(HttpStatus.UNPROCESSABLE_ENTITY, new HttpHeaders(), new ConstraintViolationExceptionMessage(e));
    }

    /**
     * Handle bind exception response entity.
     *
     * @param e the e
     * @return the response entity
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class
    })
    ResponseEntity<?> handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            return response(HttpStatus.UNPROCESSABLE_ENTITY, new HttpHeaders(),
                    new BindingResultMessage(e.getBindingResult()));
        } else {
            return response(HttpStatus.BAD_REQUEST, new HttpHeaders(), e.getMessage());
        }
    }

    private static <T> ResponseEntity<T> response(HttpStatus status, HttpHeaders headers, T body) {
        return new ResponseEntity<T>(body, headers, status);
    }
}
