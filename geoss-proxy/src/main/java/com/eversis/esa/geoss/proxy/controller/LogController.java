package com.eversis.esa.geoss.proxy.controller;

import com.eversis.esa.geoss.proxy.document.ElementClickDoc;
import com.eversis.esa.geoss.proxy.document.ResourceErrorDoc;
import com.eversis.esa.geoss.proxy.document.SearchResultDoc;
import com.eversis.esa.geoss.proxy.document.SignInDoc;
import com.eversis.esa.geoss.proxy.domain.LoggedElementClickModel;
import com.eversis.esa.geoss.proxy.domain.LoggedResourceErrorModel;
import com.eversis.esa.geoss.proxy.domain.LoggedSearchResultModel;
import com.eversis.esa.geoss.proxy.domain.LoggedSignInModel;
import com.eversis.esa.geoss.proxy.service.LogService;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The type Log controller.
 */
@Log4j2
@RestController
@RequestMapping(value = "/rest/log", produces = APPLICATION_JSON_VALUE)
@Validated
public class LogController {

    private final LogService logService;

    /**
     * Instantiates a new Log controller.
     *
     * @param logService the log service
     */
    public LogController(LogService logService) {
        this.logService = logService;
    }

    /**
     * Log search result search result doc.
     *
     * @param loggedSearchResultModel the logged search result model
     * @return the search result doc
     */
    @PostMapping(value = "logSearchResult")
    @ResponseStatus(HttpStatus.CREATED)
    SearchResultDoc logSearchResult(@Valid @RequestBody LoggedSearchResultModel loggedSearchResultModel) {
        log.info("loggedSearchResultModel:{}", loggedSearchResultModel);
        return logService.logSearchResult(loggedSearchResultModel);
    }

    /**
     * Log resource error resource error doc.
     *
     * @param loggedResourceErrorModel the logged resource error model
     * @return the resource error doc
     */
    @PostMapping(value = "logResourceError")
    @ResponseStatus(HttpStatus.CREATED)
    ResourceErrorDoc logResourceError(@Valid @RequestBody LoggedResourceErrorModel loggedResourceErrorModel) {
        log.info("loggedResourceErrorModel:{}", loggedResourceErrorModel);
        return logService.logResourceError(loggedResourceErrorModel);
    }

    /**
     * Log element click element click doc.
     *
     * @param loggedElementClickModel the logged element click model
     * @return the element click doc
     */
    @PostMapping(value = "logElementClick")
    @ResponseStatus(HttpStatus.CREATED)
    ElementClickDoc logElementClick(@Valid @RequestBody LoggedElementClickModel loggedElementClickModel) {
        log.info("loggedElementClickModel:{}", loggedElementClickModel);
        return logService.logElementClick(loggedElementClickModel);
    }

    /**
     * Log sign in sign in doc.
     *
     * @param loggedSignInModel the logged sign in model
     * @return the sign in doc
     */
    @PostMapping(value = "logSignIn")
    @ResponseStatus(HttpStatus.CREATED)
    SignInDoc logSignIn(@Valid @RequestBody LoggedSignInModel loggedSignInModel) {
        log.info("loggedSignInModel:{}", loggedSignInModel);
        return logService.logSignIn(loggedSignInModel);
    }

}
