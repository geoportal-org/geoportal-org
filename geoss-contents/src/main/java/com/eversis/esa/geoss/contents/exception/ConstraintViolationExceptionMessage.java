package com.eversis.esa.geoss.contents.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import org.springframework.data.rest.webmvc.support.RepositoryConstraintViolationExceptionMessage.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Constraint violation exception message.
 */
@Getter
public class ConstraintViolationExceptionMessage {

    @JsonProperty("errors")
    private final List<ValidationError> errors = new ArrayList<>();

    /**
     * Instantiates a new Constraint violation exception message.
     *
     * @param exception the exception
     */
    public ConstraintViolationExceptionMessage(ConstraintViolationException exception) {
        for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
            this.errors.add(
                    ValidationError.of(
                            constraintViolation.getLeafBean().getClass().getSimpleName(),
                            property(constraintViolation.getPropertyPath().toString()),
                            constraintViolation.getInvalidValue(),
                            constraintViolation.getMessage()
                    )
            );
        }
    }

    private String property(String path) {
        return path.substring(path.lastIndexOf('.') + 1);
    }
}
