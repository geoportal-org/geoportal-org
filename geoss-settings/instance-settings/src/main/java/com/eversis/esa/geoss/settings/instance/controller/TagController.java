package com.eversis.esa.geoss.settings.instance.controller;

import com.eversis.esa.geoss.common.hateoas.PageMapper;
import com.eversis.esa.geoss.settings.common.model.AuditableModel;
import com.eversis.esa.geoss.settings.common.model.VersionedModel;
import com.eversis.esa.geoss.settings.common.properties.GeossProperties;
import com.eversis.esa.geoss.settings.common.web.HttpHeadersUtil;
import com.eversis.esa.geoss.settings.instance.domain.Tag;
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
import java.util.Optional;

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
     * Gets localized tags.
     *
     * @param pageable the pageable
     * @param headers the headers
     * @return the localized tags
     */
    @Operation(hidden = true)
    @GetMapping(value = {"/tags", ""}, headers = ACCEPT_LANGUAGE)
    ResponseEntity<PagedModel<?>> getLocalizedTags(@ParameterObject @PageableDefault Pageable pageable,
            @RequestHeader HttpHeaders headers) {
        Locale acceptLocale = HttpHeadersUtil.getSupportedAcceptLanguageAsLocale(headers,
                geossProperties.getSupportedLanguages());
        log.debug("acceptLocale:{}", acceptLocale);
        Page<TagModel> tagModels = tagService.getLocalizedTags(pageable, acceptLocale);
        return toPagedModel(tagModels, acceptLocale);
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
    ResponseEntity<EntityModel<TagModel>> getLocalizedTag(@PathVariable Long id, @RequestHeader HttpHeaders headers) {
        Locale acceptLocale = HttpHeadersUtil.getSupportedAcceptLanguageAsLocale(headers,
                geossProperties.getSupportedLanguages());
        log.debug("acceptLocale:{}", acceptLocale);
        TagModel tagModel = tagService.getLocalizedTag(id, acceptLocale);
        return toResponseEntity(tagModel, HttpMethod.GET, headers);
    }

    private ResponseEntity<PagedModel<?>> toPagedModel(Page<TagModel> tagModels, Locale locale) {
        if (tagModels.getContent().isEmpty()) {
            PagedModel<?> pagedModel = pageMapper.toEmptyModel(tagModels, TagModel.class, this::tagLinks);
            pagedModel = pageMapper.addPaginationLinks(pagedModel, tagModels, Optional.empty());
            return new ResponseEntity<>(pagedModel, HttpStatus.OK);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLanguage(locale);

        PagedModel<EntityModel<TagModel>> entityModels = pageMapper.toPagedModel(tagModels, this::tagLinks,
                this::tagLinks);
        entityModels = pageMapper.addPaginationLinks(entityModels, tagModels, Optional.empty());
        return new ResponseEntity<>(entityModels, responseHeaders, HttpStatus.OK);
    }

    private ResponseEntity<EntityModel<TagModel>> toResponseEntity(
            TagModel tagModel, HttpMethod httpMethod, HttpHeaders requestHeaders) {
        VersionedModel versionedModel = tagModel.getVersioned();
        AuditableModel auditableModel = tagModel.getAuditable();
        Locale locale = tagModel.getLocale();

        HttpHeaders responseHeaders = HttpHeadersUtil.responseHeaders(versionedModel, auditableModel);
        responseHeaders.setContentLanguage(locale);

        return HttpHeadersUtil.isNotModified(versionedModel, auditableModel, httpMethod, requestHeaders) //
                ? new ResponseEntity<>(responseHeaders, HttpStatus.NOT_MODIFIED) //
                : new ResponseEntity<>(
                        EntityModel.of(tagModel, tagLinks(tagModel)),
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

    private List<Link> tagLinks() {
        return List.of(
                entityLinks.linkToCollectionResource(Tag.class)
        );
    }
}
