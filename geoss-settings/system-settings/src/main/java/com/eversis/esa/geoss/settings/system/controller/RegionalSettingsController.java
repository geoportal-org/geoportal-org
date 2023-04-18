package com.eversis.esa.geoss.settings.system.controller;

import com.eversis.esa.geoss.settings.common.model.AuditableModel;
import com.eversis.esa.geoss.settings.common.model.VersionedModel;
import com.eversis.esa.geoss.settings.common.properties.GeossProperties;
import com.eversis.esa.geoss.settings.common.web.ETagHeader;
import com.eversis.esa.geoss.settings.common.web.HttpHeadersUtil;
import com.eversis.esa.geoss.settings.system.domain.RegionalSettings;
import com.eversis.esa.geoss.settings.system.domain.RegionalSettingsKey;
import com.eversis.esa.geoss.settings.system.model.RegionalSettingsModel;
import com.eversis.esa.geoss.settings.system.service.RegionalSettingsService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The type Regional settings controller.
 */
@Log4j2
@RequiredArgsConstructor
@ExposesResourceFor(RegionalSettings.class)
@BasePathAwareController("/regional-settings")
@ResponseBody
@Tag(name = "regional-settings")
public class RegionalSettingsController {

    private final GeossProperties geossProperties;

    private final RegionalSettingsService regionalSettingsService;

    private final EntityLinks entityLinks;

    /**
     * All currencies set.
     *
     * @return the set
     */
    @Hidden
    @RequestMapping(path = "/all/currencies", method = RequestMethod.OPTIONS)
    Set<String> allCurrencies() {
        return Currency.getAvailableCurrencies().stream()
                .map(Currency::toString)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * All countries set.
     *
     * @return the set
     */
    @Hidden
    @RequestMapping(path = "/all/countries", method = RequestMethod.OPTIONS)
    Set<String> allCountries() {
        return Arrays.stream(Locale.getISOCountries())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * All languages set.
     *
     * @return the set
     */
    @Hidden
    @RequestMapping(path = "/all/languages", method = RequestMethod.OPTIONS)
    Set<String> allLanguages() {
        return Arrays.stream(Locale.getISOLanguages())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * All locales set.
     *
     * @return the set
     */
    @RequestMapping(path = "/all/locales", method = RequestMethod.OPTIONS)
    Set<String> allLocales() {
        return Arrays.stream(Locale.getAvailableLocales())
                .map(Locale::toString)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * All time zones set.
     *
     * @return the set
     */
    @RequestMapping(path = "/all/timezones", method = RequestMethod.OPTIONS)
    Set<String> allTimeZones() {
        return Arrays.stream(TimeZone.getAvailableIDs())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Supported languages set.
     *
     * @return the set
     */
    @RequestMapping(path = "/supported/languages", method = RequestMethod.OPTIONS)
    Set<String> supportedLanguages() {
        return Arrays.stream(geossProperties.getSupportedLanguages())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Head regional settings response entity.
     *
     * @return the response entity
     */
    @Hidden
    @RequestMapping(method = RequestMethod.HEAD, path = "/current")
    ResponseEntity<?> headRegionalSettings() {
        RegionalSettingsModel regionalSettingsModel = regionalSettingsService.getRegionalSettings(
                RegionalSettingsKey.CURRENT);
        HttpHeaders headers = new HttpHeaders();
        Links links = Links.of(regionalSettingsLinks(regionalSettingsModel));
        headers.add(HttpHeaders.LINK, links.toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Gets regional settings.
     *
     * @param httpHeaders the http headers
     * @return the regional settings
     */
    @GetMapping("/current")
    ResponseEntity<EntityModel<RegionalSettingsModel>> getRegionalSettings(@RequestHeader HttpHeaders httpHeaders) {
        RegionalSettingsModel regionalSettingsModel = regionalSettingsService.getRegionalSettings(
                RegionalSettingsKey.CURRENT);
        return toResponseEntity(regionalSettingsModel, HttpMethod.GET, httpHeaders);
    }

    /**
     * Put regional settings response entity.
     *
     * @param regionalSettingsModel the regional settings model
     * @param httpHeaders the http headers
     * @return the response entity
     */
    @PutMapping("/current")
    ResponseEntity<EntityModel<RegionalSettingsModel>> putRegionalSettings(
            @Valid @RequestBody RegionalSettingsModel regionalSettingsModel,
            @RequestHeader HttpHeaders httpHeaders) {

        regionalSettingsModel = regionalSettingsService.createOrUpdateRegionalSettings(RegionalSettingsKey.CURRENT,
                regionalSettingsModel, getVersionChecker(httpHeaders));
        return toResponseEntity(regionalSettingsModel, HttpMethod.PUT, httpHeaders);
    }

    /**
     * Patch regional settings response entity.
     *
     * @param regionalSettingsModel the regional settings model
     * @param httpHeaders the http headers
     * @return the response entity
     */
    @PatchMapping("/current")
    ResponseEntity<EntityModel<RegionalSettingsModel>> patchRegionalSettings(
            RegionalSettingsModel regionalSettingsModel,
            @RequestHeader HttpHeaders httpHeaders) {
        regionalSettingsModel = regionalSettingsService.updateRegionalSettings(RegionalSettingsKey.CURRENT,
                regionalSettingsModel, getVersionChecker(httpHeaders));
        return toResponseEntity(regionalSettingsModel, HttpMethod.PATCH, httpHeaders);
    }

    private ResponseEntity<EntityModel<RegionalSettingsModel>> toResponseEntity(
            RegionalSettingsModel regionalSettingsModel, HttpMethod httpMethod, HttpHeaders requestHeaders) {
        VersionedModel versionedModel = regionalSettingsModel.getVersioned();
        AuditableModel auditableModel = regionalSettingsModel.getAuditable();

        HttpHeaders responseHeaders = HttpHeadersUtil.responseHeaders(versionedModel, auditableModel);
        if (HttpMethod.PUT.equals(httpMethod) || HttpMethod.PATCH.equals(httpMethod)) {
            addLocationHeader(responseHeaders, regionalSettingsModel.getId());
        }

        return HttpHeadersUtil.isNotModified(versionedModel, auditableModel, httpMethod, requestHeaders) //
                ? new ResponseEntity<>(responseHeaders, HttpStatus.NOT_MODIFIED) //
                : new ResponseEntity<>(
                        EntityModel.of(regionalSettingsModel, regionalSettingsLinks(regionalSettingsModel)),
                        responseHeaders, HttpStatus.OK) //
                ;
    }

    private void addLocationHeader(HttpHeaders headers, Object id) {
        Link link = entityLinks.linkToItemResource(RegionalSettings.class, id);
        headers.setLocation(link.withSelfRel().toUri());
    }

    private List<Link> regionalSettingsLinks(RegionalSettingsModel regionalSettings) {
        if (regionalSettings == null) {
            return Collections.emptyList();
        }
        return List.of(
                entityLinks.linkToItemResource(RegionalSettings.class, regionalSettings.getId())
        );
    }

    private static Consumer<Long> getVersionChecker(HttpHeaders httpHeaders) {
        return version -> {
            ETagHeader etag = ETagHeader.fromIfMatch(httpHeaders);
            etag.verify(String.valueOf(version));
        };
    }
}
