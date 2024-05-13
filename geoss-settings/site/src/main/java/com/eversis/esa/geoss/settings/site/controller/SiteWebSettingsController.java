package com.eversis.esa.geoss.settings.site.controller;

import com.eversis.esa.geoss.common.hateoas.PageMapper;
import com.eversis.esa.geoss.common.hateoas.SimpleLinkBuilder;
import com.eversis.esa.geoss.settings.system.domain.WebSettings;
import com.eversis.esa.geoss.settings.system.domain.WebSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.WebSettingsSet;
import com.eversis.esa.geoss.settings.system.service.WebSettingsService;

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
 * The type Site web settings controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController
@ResponseBody
@Tags({@Tag(name = "site"), @Tag(name = "web-settings")})
public class SiteWebSettingsController {

    protected static final String WEB_SETTINGS = "/web-settings";

    private final WebSettingsService webSettingsService;

    private final PageMapper pageMapper;

    private final EntityLinks entityLinks;

    private final BaseUri baseUri;

    /**
     * Head site web settings response entity.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the response entity
     */
    @Hidden
    @RequestMapping(path = "/sites/{siteId}/web-settings", method = RequestMethod.HEAD)
    ResponseEntity<?> headSiteWebSettings(@PathVariable Long siteId, Pageable pageable) {
        Page<WebSettings> webSettings = webSettingsService.getSiteWebSettings(siteId, pageable);
        if (null == webSettings) {
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        Links links = Links.of(webSettingsLinks(siteId, true));
        headers.add(HttpHeaders.LINK, links.toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Head site web settings response entity.
     *
     * @param siteId the site id
     * @param webSettingsSet the web settings set
     * @return the response entity
     */
    @Hidden
    @RequestMapping(path = "/sites/{siteId}/web-settings/sets/{set}", method = RequestMethod.HEAD)
    ResponseEntity<?> headSiteWebSettings(@PathVariable Long siteId,
            @PathVariable("set") WebSettingsSet webSettingsSet) {
        List<WebSettings> webSettings = webSettingsService.getSiteWebSettings(siteId, webSettingsSet);
        if (null == webSettings) {
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        Links links = Links.of(webSettingsLinks(siteId, false));
        headers.add(HttpHeaders.LINK, links.toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Head site web settings response entity.
     *
     * @param siteId the site id
     * @param webSettingsSet the web settings set
     * @param webSettingsKey the web settings key
     * @return the response entity
     */
    @Hidden
    @RequestMapping(path = "/sites/{siteId}/web-settings/sets/{set}/keys/{key}", method = RequestMethod.HEAD)
    ResponseEntity<?> headSiteWebSettings(@PathVariable Long siteId,
            @PathVariable("set") WebSettingsSet webSettingsSet,
            @PathVariable("key") WebSettingsKey webSettingsKey) {
        WebSettings webSettings = webSettingsService.getSiteWebSettings(siteId, webSettingsSet, webSettingsKey);
        if (null == webSettings) {
            throw new ResourceNotFoundException();
        }
        HttpHeaders headers = new HttpHeaders();
        Links links = Links.of(webSettingsLinks(webSettings));
        headers.add(HttpHeaders.LINK, links.toString());
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    /**
     * Gets site web settings.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the site web settings
     */
    @GetMapping("/sites/{siteId}/web-settings")
    PagedModel<EntityModel<WebSettings>> getSiteWebSettings(@Parameter(example = "0") @PathVariable Long siteId,
            @ParameterObject @PageableDefault Pageable pageable) {
        Page<WebSettings> webSettings = webSettingsService.getSiteWebSettings(siteId, pageable);
        return pageMapper.toPagedModel(webSettings, WebSettings.class, this::webSettingsLinks,
                () -> webSettingsLinks(siteId, true));
    }

    /**
     * Gets site web settings.
     *
     * @param siteId the site id
     * @param webSettingsSet the web settings set
     * @return the site web settings
     */
    @GetMapping("/sites/{siteId}/web-settings/sets/{set}")
    CollectionModel<EntityModel<WebSettings>> getSiteWebSettings(@Parameter(example = "0") @PathVariable Long siteId,
            @PathVariable("set") WebSettingsSet webSettingsSet) {
        List<WebSettings> webSettings = webSettingsService.getSiteWebSettings(siteId, webSettingsSet);
        return pageMapper.toCollectionModel(webSettings, this::webSettingsLinks,
                () -> webSettingsLinks(siteId, false));
    }

    /**
     * Gets site web settings.
     *
     * @param siteId the site id
     * @param webSettingsSet the web settings set
     * @param webSettingsKey the web settings key
     * @return the site web settings
     */
    @GetMapping("/sites/{siteId}/web-settings/sets/{set}/keys/{key}")
    EntityModel<WebSettings> getSiteWebSettings(@Parameter(example = "0") @PathVariable Long siteId,
            @PathVariable("set") WebSettingsSet webSettingsSet,
            @PathVariable("key") WebSettingsKey webSettingsKey) {
        WebSettings webSettings = webSettingsService.getSiteWebSettings(siteId, webSettingsSet, webSettingsKey);
        return EntityModel.of(webSettings, webSettingsLinks(webSettings));
    }

    /**
     * Delete site web settings.
     *
     * @param siteId the site id
     */
    @DeleteMapping("/sites/{siteId}/web-settings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSiteWebSettings(@PathVariable Long siteId) {
        webSettingsService.deleteSiteWebSettings(siteId);
    }

    private List<Link> webSettingsLinks(Long siteId, boolean pageable) {
        final String basePath = baseUri.getUri().toString();
        final String requestUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        List<Link> links = new ArrayList<>();
        links.add(entityLinks.linkToCollectionResource(WebSettings.class));
        links.add(pageMapper.linkToCollectionResource(
                SimpleLinkBuilder.linkTo(basePath, "/sites/", String.valueOf(siteId), "/web-settings"),
                "siteWebSettings"));
        if (!pageable) {
            links.add(Link.of(requestUri).withSelfRel());
        }
        return links;
    }

    private List<Link> webSettingsLinks(WebSettings webSettings) {
        if (webSettings.getId() == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(
                entityLinks.linkToItemResource(WebSettings.class, webSettings.getId()).withSelfRel(),
                entityLinks.linkToItemResource(WebSettings.class, webSettings.getId()),
                linkTo(methodOn(SiteWebSettingsController.class).getSiteWebSettings(webSettings.getSiteId(),
                        webSettings.getSet(), webSettings.getKey()))
                        .withRel("siteApiSetting")
        );
    }
}
