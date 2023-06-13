package com.eversis.esa.geoss.common.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Data rest repository exception handler.
 */
@Log4j2
@ControllerAdvice(
        annotations = {
                RestController.class,
                BasePathAwareController.class,
                RepositoryRestController.class,
                RepositoryRestResource.class
        })
public class DataRestRepositoryExceptionHandler extends RepositoryRestExceptionHandler {

    /**
     * Instantiates a new Data rest repository exception handler.
     *
     * @param messageSource the message source
     */
    public DataRestRepositoryExceptionHandler(MessageSource messageSource) {
        super(messageSource);
    }
}
