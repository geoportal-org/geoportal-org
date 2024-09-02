package com.eversis.esa.geoss.proxy.controller;

import com.eversis.esa.geoss.proxy.domain.ViewCounter;
import com.eversis.esa.geoss.proxy.domain.ViewCounterModel;
import com.eversis.esa.geoss.proxy.service.ViewCounterService;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The type View counter controller.
 */
@Log4j2
@RestController
@RequestMapping(value = "/rest/counter/view", produces = APPLICATION_JSON_VALUE)
@Validated
public class ViewCounterController {

    private final ViewCounterService viewCounterService;

    /**
     * Instantiates a new View counter controller.
     *
     * @param viewCounterService the view counter service
     */
    public ViewCounterController(ViewCounterService viewCounterService) {
        this.viewCounterService = viewCounterService;
    }

    /**
     * Increase view counter response entity.
     *
     * @param viewCounterModel the view counter model
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<Void> increaseViewCounter(@Valid @RequestBody ViewCounterModel viewCounterModel) {
        log.info("Increasing view counter: {}", viewCounterModel);
        viewCounterService.increaseCounter(viewCounterModel);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Gets view counter for single entry.
     *
     * @param entryId the entry id
     * @return the view counter for single entry
     */
    @GetMapping("/{entryId}")
    public ResponseEntity<ViewCounter> getViewCounterForSingleEntry(@PathVariable @NotBlank String entryId) {
        log.info("Getting view counter for entryId: {}", entryId);
        ViewCounter viewCounter = viewCounterService.getCounter(entryId);
        return ResponseEntity.ok(viewCounter);
    }

    /**
     * Gets view counters for multiple entries.
     *
     * @param entryIds the entry ids
     * @return the view counters for multiple entries
     */
    @GetMapping("/list")
    public ResponseEntity<List<ViewCounter>> getViewCountersForMultipleEntries(
            @RequestParam @NotEmpty List<String> entryIds) {
        log.info("Getting view counters for entryIds: {}", entryIds);
        List<ViewCounter> viewCounter = viewCounterService.getCounter(entryIds);
        return ResponseEntity.ok(viewCounter);
    }

}
