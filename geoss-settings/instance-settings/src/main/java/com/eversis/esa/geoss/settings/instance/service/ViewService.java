package com.eversis.esa.geoss.settings.instance.service;

import com.eversis.esa.geoss.settings.instance.model.ViewModel;
import com.eversis.esa.geoss.settings.instance.model.ViewOptionModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface View service.
 */
public interface ViewService {

    /**
     * Gets view options.
     *
     * @param viewId the view id
     * @return the view options
     */
    List<ViewOptionModel> getViewOptions(Long viewId);

    /**
     * Remove view options.
     *
     * @param id the id
     */
    void removeViewOptions(Long id);

    /**
     * Add view option view option model.
     *
     * @param id the id
     * @param viewOptionModel the view option model
     * @return the view option model
     */
    ViewOptionModel addViewOption(Long id, ViewOptionModel viewOptionModel);

    /**
     * Gets view option.
     *
     * @param id the id
     * @param optionId the option id
     * @return the view option
     */
    ViewOptionModel getViewOption(Long id, Long optionId);

    /**
     * Sets view option.
     *
     * @param id the id
     * @param optionId the option id
     * @param viewOptionModel the view option model
     * @return the view option
     */
    ViewOptionModel setViewOption(Long id, Long optionId, ViewOptionModel viewOptionModel);

    /**
     * Remove view option.
     *
     * @param id the id
     * @param optionId the option id
     */
    void removeViewOption(Long id, Long optionId);

    /**
     * Update view option view option model.
     *
     * @param id the id
     * @param optionId the option id
     * @param viewOptionModel the view option model
     * @return the view option model
     */
    ViewOptionModel updateViewOption(Long id, Long optionId, ViewOptionModel viewOptionModel);

    /**
     * Gets site views.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the site views
     */
    Page<ViewModel> getSiteViews(Long siteId, Pageable pageable);

    /**
     * Delete site views.
     *
     * @param siteId the site id
     */
    void deleteSiteViews(Long siteId);
}
