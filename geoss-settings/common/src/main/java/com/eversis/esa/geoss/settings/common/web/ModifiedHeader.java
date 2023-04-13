package com.eversis.esa.geoss.settings.common.web;

import com.eversis.esa.geoss.settings.common.model.AuditableModel;

import org.springframework.http.HttpHeaders;

import java.time.Instant;
import java.util.Optional;

/**
 * The type Modified header.
 */
public class ModifiedHeader {

    private final long value;

    private ModifiedHeader(long value) {
        this.value = value;
    }

    /**
     * From if modified since modified header.
     *
     * @param httpHeaders the http headers
     * @return the modified header
     */
    public static ModifiedHeader fromIfModifiedSince(HttpHeaders httpHeaders) {
        long ifModifiedSince = httpHeaders.getIfModifiedSince();
        return new ModifiedHeader(ifModifiedSince);
    }

    /**
     * Is object still valid boolean.
     *
     * @param lastModified the last modified
     * @return the boolean
     */
    public boolean isObjectStillValid(Instant lastModified) {
        if (this.value == -1 || lastModified == null) {
            return false;
        }
        return Optional.of(lastModified)
                .map(Instant::toEpochMilli)
                .map(it -> it / 1000 * 1000 <= this.value)
                .orElse(true);
    }

    /**
     * Is object still valid boolean.
     *
     * @param auditableModel the auditable model
     * @return the boolean
     */
    public boolean isObjectStillValid(AuditableModel auditableModel) {
        if (auditableModel == null) {
            return false;
        }
        return isObjectStillValid(auditableModel.getLastModifiedDate());
    }
}
