package com.eversis.esa.geoss.curatedrelationships.search.model;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * The type Date range.
 */
public class DateRange {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    /**
     * Instantiates a new Date range.
     *
     * @param startDateTime the start date time
     * @param endDateTime the end date time
     */
    public DateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    /**
     * Gets start date time.
     *
     * @return the start date time
     */
    public Optional<LocalDateTime> getStartDateTime() {
        return Optional.ofNullable(startDateTime);
    }

    /**
     * Gets end date time.
     *
     * @return the end date time
     */
    public Optional<LocalDateTime> getEndDateTime() {
        return Optional.ofNullable(endDateTime);
    }
}
