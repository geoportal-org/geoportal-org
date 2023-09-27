package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.webflux;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import org.springframework.batch.core.BatchStatus;

import java.time.LocalDateTime;

/**
 * The type Job model.
 */
@JsonInclude(Include.NON_NULL)
@Data
public class JobModel {

    private final String name;

    private BatchStatus status = null;

    private LocalDateTime startTime = null;

    private LocalDateTime createTime = null;

    private LocalDateTime endTime = null;

    private LocalDateTime lastUpdated = null;
}
