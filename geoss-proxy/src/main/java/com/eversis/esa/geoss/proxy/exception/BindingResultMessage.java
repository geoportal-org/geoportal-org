package com.eversis.esa.geoss.proxy.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.support.RepositoryConstraintViolationExceptionMessage.ValidationError;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Binding result message.
 */
@Log4j2
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BindingResultMessage {

    @JsonProperty("errors")
    private final List<ValidationError> errors = new ArrayList<>();

    @JsonProperty("globalErrors")
    private final List<GlobalError> globalErrors = new ArrayList<>();

    /**
     * Instantiates a new Binding result message.
     *
     * @param bindingResult the binding result
     */
    public BindingResultMessage(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            this.errors.add(
                    ValidationError.of(
                            capitalize(fieldError.getObjectName()),
                            fieldError.getField(),
                            fieldError.getRejectedValue(),
                            fieldError.getDefaultMessage()
                    )
            );
        }
        List<ObjectError> globalErrors = bindingResult.getGlobalErrors();
        for (ObjectError objectError : globalErrors) {
            this.globalErrors.add(
                    GlobalError.of(
                            capitalize(objectError.getObjectName()),
                            objectError.getDefaultMessage()
                    )
            );
        }
    }

    /**
     * The type Global error.
     */
    public record GlobalError(String name, String message) {

        /**
         * Of global error.
         *
         * @param name the name
         * @param message the message
         * @return the global error
         */
        public static GlobalError of(String name, String message) {
            return new GlobalError(name, message);
        }

    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
