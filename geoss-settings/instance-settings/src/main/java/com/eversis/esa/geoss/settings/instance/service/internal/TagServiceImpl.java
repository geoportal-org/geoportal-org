package com.eversis.esa.geoss.settings.instance.service.internal;

import com.eversis.esa.geoss.common.constraints.AvailableLocale;
import com.eversis.esa.geoss.settings.common.model.AuditableModel;
import com.eversis.esa.geoss.settings.common.model.VersionedModel;
import com.eversis.esa.geoss.settings.instance.domain.Tag;
import com.eversis.esa.geoss.settings.instance.model.LocalizedTagModel;
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
    public LocalizedTagModel getLocalizedTag(Long id, Locale locale) {
        log.debug("id:{},locale:{}", id, locale);
        return tagRepository.findById(id)
                .map(tag -> {
                    log.debug("tag:{}", tag);
                    LocalizedTagModel localizedTagModel = tagToTagModel(tag, locale);
                    log.debug("localizedTagModel:{}", localizedTagModel);
                    return localizedTagModel;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));
    }

    @Override
    public Page<LocalizedTagModel> getLocalizedTags(Pageable pageable, Locale locale) {
        log.debug("pageable:{},locale:{}", pageable, locale);
        Page<Tag> tags = tagRepository.findAll(pageable);
        return tags.map(tag -> {
            log.trace("tag:{}", tag);
            LocalizedTagModel localizedTagModel = tagToTagModel(tag, locale);
            log.trace("localizedTagModel:{}", localizedTagModel);
            return localizedTagModel;
        });
    }

    private LocalizedTagModel tagToTagModel(Tag tag, Locale locale) {
        Locale language = Locale.forLanguageTag(locale.toLanguageTag());
        LocalizedTagModel localizedTagModel = new LocalizedTagModel();
        localizedTagModel.setId(tag.getId());
        localizedTagModel.setName(tag.getName());
        localizedTagModel.setShow(tag.isShow());
        localizedTagModel.setType(tag.getType());
        localizedTagModel.setPlacement(tag.getPlacement());
        localizedTagModel.setLocale(locale);
        Map<@AvailableLocale Locale, String> titles = tag.getTitle();
        if (titles != null) {
            localizedTagModel.setTitle(titles.get(language));
        }
        Map<@AvailableLocale Locale, String> descriptions = tag.getDescription();
        if (descriptions != null) {
            localizedTagModel.setDescription(descriptions.get(language));
        }
        VersionedModel versionedModel = conversionService.convert(tag.getVersion(), VersionedModel.class);
        localizedTagModel.setVersioned(versionedModel);
        AuditableModel auditableModel = conversionService.convert(tag.getAuditable(), AuditableModel.class);
        localizedTagModel.setAuditable(auditableModel);
        return localizedTagModel;
    }
}
