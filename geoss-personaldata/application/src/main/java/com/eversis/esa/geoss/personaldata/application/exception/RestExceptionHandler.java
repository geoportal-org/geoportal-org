package com.eversis.esa.geoss.personaldata.application.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.ExceptionMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

/**
 * The type Rest exception handler.
 */
@Log4j2
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(
        annotations = {
                RestController.class,
                BasePathAwareController.class,
                RepositoryRestController.class,
                RepositoryRestResource.class
        })
public class RestExceptionHandler {

    /**
     * Handle conversion failed exception response entity.
     *
     * @param conversionFailedException the conversion failed exception
     * @return the response entity
     */
    @ExceptionHandler
    ResponseEntity<?> handleConversionFailedException(ConversionFailedException conversionFailedException) {
        Throwable cause = conversionFailedException.getCause();
        if (cause instanceof EntityNotFoundException entityNotFoundException) {
            log.warn(buildLogMessage(entityNotFoundException));
            return handleEntityNotFoundException(entityNotFoundException);
        }
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, new HttpHeaders(), conversionFailedException);
    }

    /**
     * Handle not found response entity.
     *
     * @param entityNotFoundException the entity not found exception
     * @return the response entity
     */
    @ExceptionHandler
    ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        return response(HttpStatus.NOT_FOUND, new HttpHeaders(), null);
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

    protected String buildLogMessage(Exception ex) {
        return "Resolved [" + LogFormatUtils.formatValue(ex, -1, true) + "]";
    }

    private static ResponseEntity<ExceptionMessage> errorResponse(HttpStatus status, HttpHeaders headers,
            Exception exception) {
        if (exception != null) {
            String message = exception.getMessage();
            // LOG.debug(LogFormatUtils.formatValue(message, -1, true), exception);
            log.debug(LogFormatUtils.formatValue(message, -1, true), exception);
            if (StringUtils.hasText(message)) {
                return response(status, headers, new ExceptionMessage(exception));
            }
        }
        return response(status, headers, null);
    }

    private static <T> ResponseEntity<T> response(HttpStatus status, HttpHeaders headers, T body) {
        return new ResponseEntity<>(body, headers, status);
    }
}
