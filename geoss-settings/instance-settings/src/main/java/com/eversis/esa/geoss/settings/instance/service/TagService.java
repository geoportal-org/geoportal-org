package com.eversis.esa.geoss.settings.instance.service;

import com.eversis.esa.geoss.settings.instance.model.LocalizedTagModel;
import com.eversis.esa.geoss.settings.instance.model.TagModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Locale;

/**
 * The interface Tag service.
 */
public interface TagService {

    /**
     * Gets tags.
     *
     * @param pageable the pageable
     * @return the tags
     */
    Page<TagModel> getTags(Pageable pageable);

    /**
     * Gets tag.
     *
     * @param id the id
     * @return the tag
     */
    TagModel getTag(Long id);

    /**
     * Gets localized tags.
     *
     * @param pageable the pageable
     * @param locale the locale
     * @return the localized tags
     */
    Page<LocalizedTagModel> getLocalizedTags(Pageable pageable, Locale locale);

    /**
     * Gets localized tag.
     *
     * @param id the id
     * @param locale the locale
     * @return the localized tag
     */
    LocalizedTagModel getLocalizedTag(Long id, Locale locale);
}
