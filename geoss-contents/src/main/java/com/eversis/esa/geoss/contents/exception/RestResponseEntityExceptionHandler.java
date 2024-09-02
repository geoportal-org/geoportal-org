package com.eversis.esa.geoss.contents.exception;

import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
     * Handle file name not unique exception response entity.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(FileNameNotUniqueException.class)
    public ResponseEntity<ExceptionResponseMessage> handleFileNameNotUniqueException(FileNameNotUniqueException ex) {
        List<String> errors = Arrays.asList(new String[]{ex.getMessage()});
        ExceptionResponseMessage responseMessage =
                new ExceptionResponseMessage(
                        "fileName is not unique. File with the same name is already in upload folder. Change file name",
                        HttpStatus.EXPECTATION_FAILED.value(), new Date(), errors);
        return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
    }

    @Override
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        ExceptionResponseMessage responseMessage =
                new ExceptionResponseMessage("File is too large. Max file size is 50MB.",
                        HttpStatus.EXPECTATION_FAILED.value(), new Date(), errors);
        return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);
    }
}
