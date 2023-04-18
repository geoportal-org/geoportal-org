package com.eversis.esa.geoss.settings.common.web;

import com.eversis.esa.geoss.settings.common.model.AuditableModel;
import com.eversis.esa.geoss.settings.common.model.VersionedModel;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.IllformedLocaleException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

/**
 * The type Http header util.
 */
public class HttpHeadersUtil {

    /**
     * Gets supported accept language as locale.
     *
     * @param headers the headers
     * @param languages the languages
     * @return the supported accept language as locale
     */
    public static Locale getSupportedAcceptLanguageAsLocale(HttpHeaders headers, String[] supportedLanguages) {
        Set<String> languages = Arrays.stream(supportedLanguages)
                .collect(Collectors.toSet());
        List<Locale> locales = headers.getAcceptLanguageAsLocales().stream()
                .filter(locale -> languages.contains(locale.toLanguageTag()))
                .toList();
        if (locales.isEmpty()) {
            throw new IllformedLocaleException("Unsupported language: " + headers.getValuesAsList(ACCEPT_LANGUAGE));
        }
        return locales.get(0);
    }

    /**
     * Is not modified boolean.
     *
     * @param versionedModel the versioned model
     * @param auditableModel the auditable model
     * @param httpMethod the http method
     * @param requestHeaders the request headers
     * @return the boolean
     */
    public static boolean isNotModified(VersionedModel versionedModel, AuditableModel auditableModel,
            HttpMethod httpMethod, HttpHeaders requestHeaders) {

        ETagHeader etagHeader = HttpMethod.GET.equals(httpMethod) ? ETagHeader.fromIfNoneMatch(requestHeaders)
                : ETagHeader.fromIfMatch(requestHeaders);
        ModifiedHeader modifiedHeader = ModifiedHeader.fromIfModifiedSince(requestHeaders);

        return etagHeader.matches(versionedModel) && modifiedHeader.isObjectStillValid(auditableModel);
    }

    /**
     * Response headers http headers.
     *
     * @param versionedModel the versioned model
     * @param auditableModel the auditable model
     * @return the http headers
     */
    public static HttpHeaders responseHeaders(VersionedModel versionedModel, AuditableModel auditableModel) {
        HttpHeaders responseHeaders = new HttpHeaders();
        Optional.ofNullable(versionedModel)
                .map(VersionedModel::getVersion)
                .map(version -> "\"" + version + "\"")
                .ifPresent(responseHeaders::setETag);
        Optional.ofNullable(auditableModel)
                .map(AuditableModel::getLastModifiedDate)
                .ifPresent(responseHeaders::setLastModified);
        return responseHeaders;
    }
}
