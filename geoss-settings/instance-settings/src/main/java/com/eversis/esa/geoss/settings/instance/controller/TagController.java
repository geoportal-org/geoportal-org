package com.eversis.esa.geoss.settings.instance.controller;

import com.eversis.esa.geoss.common.hateoas.PageMapper;
import com.eversis.esa.geoss.settings.common.model.AuditableModel;
import com.eversis.esa.geoss.settings.common.model.VersionedModel;
import com.eversis.esa.geoss.settings.common.properties.GeossProperties;
import com.eversis.esa.geoss.settings.common.web.HttpHeadersUtil;
import com.eversis.esa.geoss.settings.instance.domain.Tag;
import com.eversis.esa.geoss.settings.instance.model.LocalizedTagModel;
import com.eversis.esa.geoss.settings.instance.model.TagModel;
import com.eversis.esa.geoss.settings.instance.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

/**
 * The type Tag controller.
 */
@Log4j2
@RequiredArgsConstructor
@RepositoryRestController("/tags")
@ResponseBody
@io.swagger.v3.oas.annotations.tags.Tag(name = "tutorial-tags")
public class TagController {

    private final TagService tagService;

    private final GeossProperties geossProperties;

    private final PageMapper pageMapper;

    private final EntityLinks entityLinks;

    /**
     * Gets tags.
     *
     * @param pageable the pageable
     * @return the tags
     */
    @Operation(hidden = true)
    @GetMapping(value = {"/tags", ""}, headers = ACCEPT_LANGUAGE + "=*")
    PagedModel<EntityModel<TagModel>> getTags(@ParameterObject @PageableDefault Pageable pageable) {
        Page<TagModel> tagModels = tagService.getTags(pageable);
        return pageMapper.toPagedModel(tagModels, TagModel.class, this::tagLinks, this::tagLinks);
    }

    /**
     * Gets localized tags.
     *
     * @param pageable the pageable
     * @param headers the headers
     * @return the localized tags
     */
    @Operation(hidden = true)
    @GetMapping(value = {"/tags", ""}, headers = ACCEPT_LANGUAGE)
    ResponseEntity<PagedModel<EntityModel<LocalizedTagModel>>> getLocalizedTags(
            @ParameterObject @PageableDefault Pageable pageable,
            @RequestHeader HttpHeaders headers) {
        Locale acceptLocale = HttpHeadersUtil.getSupportedAcceptLanguageAsLocale(headers,
                geossProperties.getSupportedLanguages());
        log.debug("acceptLocale:{}", acceptLocale);
        Page<LocalizedTagModel> tagModels = tagService.getLocalizedTags(pageable, acceptLocale);
        return toPagedModel(tagModels, acceptLocale);
    }

    /**
     * Gets tag.
     *
     * @param id the id
     * @param headers the headers
     * @return the tag
     */
    @Operation(hidden = true)
    @GetMapping(value = {"/{id}"}, headers = ACCEPT_LANGUAGE + "=*")
    ResponseEntity<EntityModel<TagModel>> getTag(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        TagModel tagModel = tagService.getTag(id);
        return toResponseEntity(tagModel, HttpMethod.GET, headers);
    }

    /**
     * Gets localized tag.
     *
     * @param id the id
     * @param headers the headers
     * @return the localized tag
     */
    @Operation(hidden = true)
    @GetMapping(value = {"/{id}"}, headers = ACCEPT_LANGUAGE)
    ResponseEntity<EntityModel<LocalizedTagModel>> getLocalizedTag(@PathVariable Long id,
            @RequestHeader HttpHeaders headers) {
        Locale acceptLocale = HttpHeadersUtil.getSupportedAcceptLanguageAsLocale(headers,
                geossProperties.getSupportedLanguages());
        log.debug("acceptLocale:{}", acceptLocale);
        LocalizedTagModel localizedTagModel = tagService.getLocalizedTag(id, acceptLocale);
        return toResponseEntity(localizedTagModel, HttpMethod.GET, headers);
    }

    private ResponseEntity<PagedModel<EntityModel<LocalizedTagModel>>> toPagedModel(Page<LocalizedTagModel> tagModels,
            Locale locale) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLanguage(locale);

        PagedModel<EntityModel<LocalizedTagModel>> entityModels = pageMapper.toPagedModel(tagModels,
                LocalizedTagModel.class,
                this::tagLinks, this::tagLinks);
        return new ResponseEntity<>(entityModels, responseHeaders, HttpStatus.OK);
    }

    private ResponseEntity<EntityModel<TagModel>> toResponseEntity(TagModel tagModel, HttpMethod httpMethod,
            HttpHeaders requestHeaders) {
        VersionedModel versionedModel = tagModel.getVersioned();
        AuditableModel auditableModel = tagModel.getAuditable();

        HttpHeaders responseHeaders = HttpHeadersUtil.responseHeaders(versionedModel, auditableModel);

        return HttpHeadersUtil.isNotModified(versionedModel, auditableModel, httpMethod, requestHeaders) //
                ? new ResponseEntity<>(responseHeaders, HttpStatus.NOT_MODIFIED) //
                : new ResponseEntity<>(
                        EntityModel.of(tagModel, tagLinks(tagModel)),
                        responseHeaders, HttpStatus.OK) //
                ;
    }

    private ResponseEntity<EntityModel<LocalizedTagModel>> toResponseEntity(
            LocalizedTagModel localizedTagModel, HttpMethod httpMethod, HttpHeaders requestHeaders) {
        VersionedModel versionedModel = localizedTagModel.getVersioned();
        AuditableModel auditableModel = localizedTagModel.getAuditable();
        Locale locale = localizedTagModel.getLocale();

        HttpHeaders responseHeaders = HttpHeadersUtil.responseHeaders(versionedModel, auditableModel);
        responseHeaders.setContentLanguage(locale);

        return HttpHeadersUtil.isNotModified(versionedModel, auditableModel, httpMethod, requestHeaders) //
                ? new ResponseEntity<>(responseHeaders, HttpStatus.NOT_MODIFIED) //
                : new ResponseEntity<>(
                        EntityModel.of(localizedTagModel, tagLinks(localizedTagModel)),
                        responseHeaders, HttpStatus.OK) //
                ;
    }

    private List<Link> tagLinks(TagModel tagModel) {
        if (tagModel == null) {
            return Collections.emptyList();
        }
        return List.of(
                entityLinks.linkToItemResource(Tag.class, tagModel.getId())
        );
    }

    private List<Link> tagLinks(LocalizedTagModel localizedTagModel) {
        if (localizedTagModel == null) {
            return Collections.emptyList();
        }
        return List.of(
                entityLinks.linkToItemResource(Tag.class, localizedTagModel.getId())
        );
    }

    private List<Link> tagLinks() {
        return List.of(
                entityLinks.linkToCollectionResource(Tag.class)
        );
    }
}
