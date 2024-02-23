package com.eversis.esa.geoss.proxy.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eversis.esa.geoss.proxy.domain.Score;
import com.eversis.esa.geoss.proxy.domain.ScoreModel;
import com.eversis.esa.geoss.proxy.service.ScoreService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Score controller.
 */
@Log4j2
@RestController
@RequestMapping(value = "/rest/score", produces = APPLICATION_JSON_VALUE)
@Validated
public class ScoreController {

    private final ScoreService scoreService;

    /**
     * Instantiates a new Score controller.
     *
     * @param scoreService the score service
     */
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    /**
     * Add score response entity.
     *
     * @param scoreModel the score model
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<Void> addScore(@Valid @RequestBody ScoreModel scoreModel){
        log.info("Adding score: {}", scoreModel);
        scoreService.addScore(scoreModel);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Get score response entity.
     *
     * @param entryId the entry id
     * @return the response entity
     */
    @GetMapping("/{entryId}")
    public ResponseEntity<Score> getScore(@PathVariable @NotBlank String entryId){
        log.info("Finding score for entryId: {}", entryId);
        Score score = scoreService.getScore(entryId);
        return ResponseEntity.ok(score);
    }

}
