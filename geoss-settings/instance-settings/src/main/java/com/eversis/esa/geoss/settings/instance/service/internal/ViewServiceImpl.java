package com.eversis.esa.geoss.settings.instance.service.internal;

import com.eversis.esa.geoss.settings.instance.model.ViewOptionModel;
import com.eversis.esa.geoss.settings.instance.service.ViewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * The type View service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class ViewServiceImpl implements ViewService {

    @Override
    public List<ViewOptionModel> getViewOptions(Long viewId) {
        log.debug("viewId:{}", viewId);
        // TODO implement me
        return Collections.emptyList();
    }

    @Override
    public void removeViewOptions(Long viewId) {
        log.debug("viewId:{}", viewId);
        // TODO implement me
    }

    @Override
    public ViewOptionModel addViewOption(Long viewId, ViewOptionModel viewOptionModel) {
        log.debug("viewId:{},viewOptionModel:{}", viewId, viewOptionModel);
        // TODO implement me
        return viewOptionModel;
    }

    @Override
    public ViewOptionModel getViewOption(Long viewId, Long optionId) {
        log.debug("viewId:{},optionId:{}", viewId, optionId);
        // TODO implement me
        return new ViewOptionModel();
    }

    @Override
    public ViewOptionModel setViewOption(Long viewId, Long optionId, ViewOptionModel viewOptionModel) {
        log.debug("viewId:{},optionId:{},viewOptionModel:{}", viewId, optionId, viewOptionModel);
        // TODO implement me
        return viewOptionModel;
    }

    @Override
    public void removeViewOption(Long viewId, Long optionId) {
        log.debug("viewId:{},optionId:{}", viewId, optionId);
        // TODO implement me
    }

    @Override
    public ViewOptionModel updateViewOption(Long viewId, Long optionId, ViewOptionModel viewOptionModel) {
        log.debug("viewId:{},optionId:{},viewOptionModel:{}", viewId, optionId, viewOptionModel);
        // TODO implement me
        return viewOptionModel;
    }
}
