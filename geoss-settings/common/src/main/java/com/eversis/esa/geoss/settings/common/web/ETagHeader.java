package com.eversis.esa.geoss.settings.common.web;

import com.eversis.esa.geoss.settings.common.model.VersionedModel;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.support.ETag;
import org.springframework.data.rest.webmvc.support.ETagDoesntMatchException;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Objects;

import static org.springframework.util.StringUtils.trimLeadingCharacter;
import static org.springframework.util.StringUtils.trimTrailingCharacter;

/**
 * The type E tag header.
 */
@Log4j2
public class ETagHeader {

    private final String value;

    private ETagHeader(String value) {
        this.value = trimTrailingCharacter(trimLeadingCharacter(value, '"'), '"');
    }

    /**
     * From if none match e tag header.
     *
     * @param httpHeaders the http headers
     * @return the e tag header
     */
    public static ETagHeader fromIfNoneMatch(HttpHeaders httpHeaders) {
        List<String> ifNoneMatch = httpHeaders.getIfNoneMatch();
        String ifNoneMatchValue = ifNoneMatch.isEmpty() ? null : ifNoneMatch.get(0);
        return new ETagHeader(ifNoneMatchValue);
    }

    /**
     * From if match e tag header.
     *
     * @param httpHeaders the http headers
     * @return the e tag header
     */
    public static ETagHeader fromIfMatch(HttpHeaders httpHeaders) {
        List<String> ifMatch = httpHeaders.getIfMatch();
        String ifMatchValue = ifMatch.isEmpty() ? null : ifMatch.get(0);
        return new ETagHeader(ifMatchValue);
    }

    /**
     * Verify.
     *
     * @param version the version
     */
    public void verify(String version) {
        if (value == null || version == null) {
            return;
        }
        if (!Objects.equals(value, version)) {
            throw new ETagDoesntMatchException(version, ETag.from(value));
        }
    }

    /**
     * Matches boolean.
     *
     * @param version the version
     * @return the boolean
     */
    public boolean matches(String version) {
        if (value == null || version == null) {
            return false;
        }
        return Objects.equals(value, version);
    }

    /**
     * Matches boolean.
     *
     * @param versionedModel the versioned model
     * @return the boolean
     */
    public boolean matches(VersionedModel versionedModel) {
        if (versionedModel == null) {
            return false;
        }
        return matches(String.valueOf(versionedModel.getVersion()));
    }

    @Override
    public String toString() {
        return value == null ? null : "\"".concat(value).concat("\"");
    }
}
