package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * The type Date util.
 */
public class DateUtil {

    private DateUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Convert to local date time local date time.
     *
     * @param dateToConvert the date to convert
     * @return the local date time
     */
    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
