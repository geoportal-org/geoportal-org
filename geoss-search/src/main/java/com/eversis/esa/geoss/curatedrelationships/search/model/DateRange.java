package com.eversis.esa.geoss.curatedrelationships.search.model;

import java.time.LocalDateTime;
import java.util.Optional;

public class DateRange {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public DateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public Optional<LocalDateTime> getStartDateTime() {
        return Optional.ofNullable(startDateTime);
    }

    public Optional<LocalDateTime> getEndDateTime() {
        return Optional.ofNullable(endDateTime);
    }
}
