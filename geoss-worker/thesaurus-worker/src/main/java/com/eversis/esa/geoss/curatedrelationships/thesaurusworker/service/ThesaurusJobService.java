package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.service;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.Status;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.ThesaurusJobModel;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.ThesaurusType;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Thesaurus job service.
 */
@Log4j2
@Service
public class ThesaurusJobService {

    private final Map<ThesaurusType, ThesaurusJobModel> jobs = new ConcurrentHashMap<>();

    /**
     * Get thesaurus job model.
     *
     * @param thesaurusType the thesaurus type
     * @return the thesaurus job model
     */
    public ThesaurusJobModel get(ThesaurusType thesaurusType) {
        log.debug("thesaurusType:{}", thesaurusType);
        ThesaurusJobModel thesaurusJobModel = jobs.computeIfAbsent(thesaurusType, this::emptyThesaurusJobModel);
        log.debug("thesaurusJobModel:{}", thesaurusJobModel);
        return thesaurusJobModel;
    }

    /**
     * Start thesaurus job model.
     *
     * @param thesaurusType the thesaurus type
     * @return the thesaurus job model
     */
    public ThesaurusJobModel start(ThesaurusType thesaurusType) {
        log.debug("thesaurusType:{}", thesaurusType);
        ThesaurusJobModel thesaurusJobModel = jobs.compute(thesaurusType, (thesaurusType1, thesaurusJobModel1) -> {
            if (thesaurusJobModel1 == null) {
                thesaurusJobModel1 = emptyThesaurusJobModel(thesaurusType1);
            }
            thesaurusJobModel1.setStatus(Status.STARTING);
            thesaurusJobModel1.setLastUpdated(LocalDateTime.now());
            return thesaurusJobModel1;
        });
        log.debug("starting:{}", thesaurusJobModel);
        return thesaurusJobModel;
    }

    /**
     * Started thesaurus job model.
     *
     * @param thesaurusType the thesaurus type
     * @return the thesaurus job model
     */
    public ThesaurusJobModel started(ThesaurusType thesaurusType) {
        log.debug("thesaurusType:{}", thesaurusType);
        ThesaurusJobModel thesaurusJobModel = jobs.computeIfPresent(thesaurusType,
                (thesaurusType1, thesaurusJobModel1) -> {
                    thesaurusJobModel1.setStatus(Status.STARTED);
                    thesaurusJobModel1.setStartTime(LocalDateTime.now());
                    thesaurusJobModel1.setLastUpdated(LocalDateTime.now());
                    return thesaurusJobModel1;
                });
        log.debug("started:{}", thesaurusJobModel);
        return thesaurusJobModel;
    }

    /**
     * Stop thesaurus job model.
     *
     * @param thesaurusType the thesaurus type
     * @return the thesaurus job model
     */
    public ThesaurusJobModel stop(ThesaurusType thesaurusType) {
        log.debug("thesaurusType:{}", thesaurusType);
        ThesaurusJobModel thesaurusJobModel = jobs.computeIfPresent(thesaurusType,
                (thesaurusType1, thesaurusJobModel1) -> {
                    thesaurusJobModel1.setStatus(Status.STOPPING);
                    thesaurusJobModel1.setLastUpdated(LocalDateTime.now());
                    return thesaurusJobModel1;
                });
        log.debug("stopping:{}", thesaurusJobModel);
        return thesaurusJobModel;
    }

    /**
     * Stopped thesaurus job model.
     *
     * @param thesaurusType the thesaurus type
     * @return the thesaurus job model
     */
    public ThesaurusJobModel stopped(ThesaurusType thesaurusType) {
        log.debug("thesaurusType:{}", thesaurusType);
        ThesaurusJobModel thesaurusJobModel = jobs.computeIfPresent(thesaurusType,
                (thesaurusType1, thesaurusJobModel1) -> {
                    thesaurusJobModel1.setStatus(Status.STOPPED);
                    thesaurusJobModel1.setEndTime(LocalDateTime.now());
                    thesaurusJobModel1.setLastUpdated(LocalDateTime.now());
                    return thesaurusJobModel1;
                });
        log.debug("stopped:{}", thesaurusJobModel);
        return thesaurusJobModel;
    }

    /**
     * Complete thesaurus job model.
     *
     * @param thesaurusType the thesaurus type
     * @return the thesaurus job model
     */
    public ThesaurusJobModel complete(ThesaurusType thesaurusType) {
        log.debug("thesaurusType:{}", thesaurusType);
        ThesaurusJobModel thesaurusJobModel = jobs.computeIfPresent(thesaurusType,
                (thesaurusType1, thesaurusJobModel1) -> {
                    thesaurusJobModel1.setStatus(Status.COMPLETED);
                    thesaurusJobModel1.setEndTime(LocalDateTime.now());
                    thesaurusJobModel1.setLastUpdated(LocalDateTime.now());
                    return thesaurusJobModel1;
                });
        log.debug("completed:{}", thesaurusJobModel);
        return thesaurusJobModel;
    }

    /**
     * Fail thesaurus job model.
     *
     * @param thesaurusType the thesaurus type
     * @param throwable the throwable
     * @return the thesaurus job model
     */
    public ThesaurusJobModel fail(ThesaurusType thesaurusType, Throwable throwable) {
        log.debug("thesaurusType:{}", thesaurusType);
        ThesaurusJobModel thesaurusJobModel = jobs.computeIfPresent(thesaurusType,
                (thesaurusType1, thesaurusJobModel1) -> {
                    thesaurusJobModel1.setStatus(Status.FAILED);
                    thesaurusJobModel1.setThrowable(throwable);
                    thesaurusJobModel1.setEndTime(LocalDateTime.now());
                    thesaurusJobModel1.setLastUpdated(LocalDateTime.now());
                    return thesaurusJobModel1;
                });
        log.debug("failed:{}", thesaurusJobModel);
        return thesaurusJobModel;
    }

    /**
     * Update thesaurus job model.
     *
     * @param thesaurusType the thesaurus type
     * @return the thesaurus job model
     */
    public ThesaurusJobModel update(ThesaurusType thesaurusType) {
        log.debug("thesaurusType:{}", thesaurusType);
        ThesaurusJobModel thesaurusJobModel = jobs.computeIfPresent(thesaurusType,
                (thesaurusType1, thesaurusJobModel1) -> {
                    thesaurusJobModel1.setLastUpdated(LocalDateTime.now());
                    return thesaurusJobModel1;
                });
        log.debug("pending:{}", thesaurusJobModel);
        return thesaurusJobModel;
    }

    private ThesaurusJobModel emptyThesaurusJobModel(ThesaurusType thesaurusType) {
        return new ThesaurusJobModel(thesaurusType);
    }
}
