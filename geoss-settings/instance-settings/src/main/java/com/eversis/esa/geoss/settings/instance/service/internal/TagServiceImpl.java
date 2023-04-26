package com.eversis.esa.geoss.settings.instance.service.internal;

import com.eversis.esa.geoss.settings.common.constraints.AvailableLocale;
import com.eversis.esa.geoss.settings.common.model.AuditableModel;
import com.eversis.esa.geoss.settings.common.model.VersionedModel;
import com.eversis.esa.geoss.settings.instance.domain.Tag;
import com.eversis.esa.geoss.settings.instance.model.TagModel;
import com.eversis.esa.geoss.settings.instance.repository.TagRepository;
import com.eversis.esa.geoss.settings.instance.service.TagService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Map;

/**
 * The type Tag service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private ConversionService conversionService;

    /**
     * Sets conversion service.
     *
     * @param conversionService the conversion service
     */
    @Autowired
    public void setConversionService(@Qualifier("mvcConversionService") ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public TagModel getLocalizedTag(Long id, Locale locale) {
        log.debug("id:{},locale:{}", id, locale);
        return tagRepository.findById(id)
                .map(tag -> {
                    log.debug("tag:{}", tag);
                    TagModel tagModel = tagToTagModel(tag, locale);
                    log.debug("tagModel:{}", tagModel);
                    return tagModel;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
    }

    @Override
    public Page<TagModel> getLocalizedTags(Pageable pageable, Locale locale) {
        log.debug("pageable:{},locale:{}", pageable, locale);
        Page<Tag> tags = tagRepository.findAll(pageable);
        return tags.map(tag -> {
            log.trace("tag:{}", tag);
            TagModel tagModel = tagToTagModel(tag, locale);
            log.trace("tagModel:{}", tagModel);
            return tagModel;
        });
    }

    private TagModel tagToTagModel(Tag tag, Locale locale) {
        Locale language = Locale.forLanguageTag(locale.toLanguageTag());
        TagModel tagModel = new TagModel();
        tagModel.setId(tag.getId());
        tagModel.setName(tag.getName());
        tagModel.setShow(tag.isShow());
        tagModel.setType(tag.getType());
        tagModel.setPlacement(tag.getPlacement());
        tagModel.setLocale(locale);
        Map<@AvailableLocale Locale, String> titles = tag.getTitle();
        if (titles != null) {
            tagModel.setTitle(titles.get(language));
        }
        Map<@AvailableLocale Locale, String> descriptions = tag.getDescription();
        if (descriptions != null) {
            tagModel.setDescription(descriptions.get(language));
        }
        VersionedModel versionedModel = conversionService.convert(tag.getVersion(), VersionedModel.class);
        tagModel.setVersioned(versionedModel);
        AuditableModel auditableModel = conversionService.convert(tag.getAuditable(), AuditableModel.class);
        tagModel.setAuditable(auditableModel);
        return tagModel;
    }
}
