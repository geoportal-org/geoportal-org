package com.eversis.esa.geoss.settings.site.controller;

import com.eversis.esa.geoss.common.hateoas.PageMapper;
import com.eversis.esa.geoss.common.hateoas.SimpleLinkBuilder;
import com.eversis.esa.geoss.settings.system.domain.ApiSettings;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsSet;
import com.eversis.esa.geoss.settings.system.service.ApiSettingsService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.BaseUri;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The type Site api settings controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController
@ResponseBody
@Tags({@Tag(name = "site"), @Tag(name = "api-settings")})
public class SiteApiSettingsController {

    private final ApiSettingsService apiSettingsService;

    private final PageMapper pageMapper;

    private final EntityLinks entityLinks;

    private final BaseUri baseUri;

    /**
     * Head site api settings response entity.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the response entity
     */
    @Hidden
    @RequestMapping(path = "/sites/{siteId}/api-settings", method = RequestMethod.HEAD)
    ResponseEntity<?> headSiteApiSettings(@PathVariable Long siteId, Pageable pageable) {
        Page<ApiSettings> apiSettings = apiSettingsService.getSiteApiSettings(siteId, pageable);
        if (null == apiSettings) {
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        Links links = Links.of(apiSettingsLinks(siteId, true));
        headers.add(HttpHeaders.LINK, links.toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Head site api settings response entity.
     *
     * @param siteId the site id
     * @param apiSettingsSet the api settings set
     * @return the response entity
     */
    @Hidden
    @RequestMapping(path = "/sites/{siteId}/api-settings/sets/{set}", method = RequestMethod.HEAD)
    ResponseEntity<?> headSiteApiSettings(@PathVariable Long siteId,
            @PathVariable("set") ApiSettingsSet apiSettingsSet) {
        List<ApiSettings> apiSettings = apiSettingsService.getSiteApiSettings(siteId, apiSettingsSet);
        if (null == apiSettings) {
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        Links links = Links.of(apiSettingsLinks(siteId, false));
        headers.add(HttpHeaders.LINK, links.toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Head site api settings response entity.
     *
     * @param siteId the site id
     * @param apiSettingsSet the api settings set
     * @param apiSettingsKey the api settings key
     * @return the response entity
     */
    @Hidden
    @RequestMapping(path = "/sites/{siteId}/api-settings/sets/{set}/keys/{key}", method = RequestMethod.HEAD)
    ResponseEntity<?> headSiteApiSettings(@PathVariable Long siteId,
            @PathVariable("set") ApiSettingsSet apiSettingsSet,
            @PathVariable("key") ApiSettingsKey apiSettingsKey) {
        ApiSettings apiSettings = apiSettingsService.getSiteApiSettings(siteId, apiSettingsSet, apiSettingsKey);
        if (null == apiSettings) {
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        Links links = Links.of(apiSettingsLinks(apiSettings));
        headers.add(HttpHeaders.LINK, links.toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Gets site api settings.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the site api settings
     */
    @GetMapping("/sites/{siteId}/api-settings")
    PagedModel<EntityModel<ApiSettings>> getSiteApiSettings(@Parameter(example = "0") @PathVariable Long siteId,
            @ParameterObject @PageableDefault Pageable pageable) {
        Page<ApiSettings> apiSettings = apiSettingsService.getSiteApiSettings(siteId, pageable);
        return pageMapper.toPagedModel(apiSettings, ApiSettings.class, this::apiSettingsLinks,
                () -> apiSettingsLinks(siteId, true));
    }

    /**
     * Gets site api settings.
     *
     * @param siteId the site id
     * @param apiSettingsSet the api settings set
     * @return the site api settings
     */
    @GetMapping("/sites/{siteId}/api-settings/sets/{set}")
    CollectionModel<EntityModel<ApiSettings>> getSiteApiSettings(@Parameter(example = "0") @PathVariable Long siteId,
            @PathVariable("set") ApiSettingsSet apiSettingsSet) {
        List<ApiSettings> apiSettings = apiSettingsService.getSiteApiSettings(siteId, apiSettingsSet);
        return pageMapper.toCollectionModel(apiSettings, this::apiSettingsLinks,
                () -> apiSettingsLinks(siteId, false));
    }

    /**
     * Gets site api settings.
     *
     * @param siteId the site id
     * @param apiSettingsSet the api settings set
     * @param apiSettingsKey the api settings key
     * @return the site api settings
     */
    @GetMapping("/sites/{siteId}/api-settings/sets/{set}/keys/{key}")
    EntityModel<ApiSettings> getSiteApiSettings(@Parameter(example = "0") @PathVariable Long siteId,
            @PathVariable("set") ApiSettingsSet apiSettingsSet,
            @PathVariable("key") ApiSettingsKey apiSettingsKey) {
        ApiSettings apiSettings = apiSettingsService.getSiteApiSettings(siteId, apiSettingsSet, apiSettingsKey);
        return EntityModel.of(apiSettings, apiSettingsLinks(apiSettings));
    }

    /**
     * Delete site api settings.
     *
     * @param siteId the site id
     */
    @DeleteMapping("/sites/{siteId}/api-settings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSiteApiSettings(@PathVariable Long siteId) {
        apiSettingsService.deleteSiteApiSettings(siteId);
    }

    private List<Link> apiSettingsLinks(Long siteId, boolean pageable) {
        final String basePath = baseUri.getUri().toString();
        final String requestUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        List<Link> links = new ArrayList<>();
        links.add(entityLinks.linkToCollectionResource(ApiSettings.class));
        links.add(pageMapper.linkToCollectionResource(
                SimpleLinkBuilder.linkTo(basePath, "/sites/", String.valueOf(siteId), "/api-settings"),
                "siteApiSettings"));
        if (!pageable) {
            links.add(Link.of(requestUri).withSelfRel());
        }
        return links;
    }

    private List<Link> apiSettingsLinks(ApiSettings apiSettings) {
        if (apiSettings.getId() == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(
                entityLinks.linkToItemResource(ApiSettings.class, apiSettings.getId()).withSelfRel(),
                entityLinks.linkToItemResource(ApiSettings.class, apiSettings.getId()),
                linkTo(methodOn(SiteApiSettingsController.class).getSiteApiSettings(apiSettings.getSiteId(),
                        apiSettings.getSet(), apiSettings.getKey()))
                        .withRel("siteApiSetting")
        );
    }
}
