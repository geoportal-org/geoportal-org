package com.eversis.esa.geoss.settings.instance.service;

import com.eversis.esa.geoss.settings.instance.model.TagModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Locale;

/**
 * The interface Tag service.
 */
public interface TagService {

    /**
     * Gets localized tags.
     *
     * @param pageable the pageable
     * @param locale the locale
     * @return the localized tags
     */
    Page<TagModel> getLocalizedTags(Pageable pageable, Locale locale);

    /**
     * Gets localized tag.
     *
     * @param id the id
     * @param locale the locale
     * @return the localized tag
     */
    TagModel getLocalizedTag(Long id, Locale locale);
}
