package com.eversis.esa.geoss.settings.instance.service.internal;

import com.eversis.esa.geoss.settings.instance.domain.View;
import com.eversis.esa.geoss.settings.instance.domain.ViewOption;
import com.eversis.esa.geoss.settings.instance.model.ViewOptionModel;
import com.eversis.esa.geoss.settings.instance.repository.ViewOptionRepository;
import com.eversis.esa.geoss.settings.instance.service.ViewService;
import com.eversis.esa.geoss.settings.instance.support.ViewOptionModelToViewOptionMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The type View service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class ViewServiceImpl implements ViewService {

    private final ViewOptionRepository viewOptionRepository;

    private final ViewOptionModelToViewOptionMapper viewOptionModelToViewOptionMapper;

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
    public List<ViewOptionModel> getViewOptions(Long viewId) {
        log.debug("viewId:{}", viewId);
        return viewOptionRepository.findByViewId(viewId)
                .map(viewOption -> conversionService.convert(viewOption, ViewOptionModel.class))
                .toList();
    }

    @Override
    public void removeViewOptions(Long viewId) {
        log.debug("viewId:{}", viewId);
        long count = viewOptionRepository.deleteAllByViewId(viewId);
        log.debug("count:{}", count);
    }

    @Override
    public ViewOptionModel addViewOption(Long viewId, ViewOptionModel viewOptionModel) {
        log.debug("viewId:{},viewOptionModel:{}", viewId, viewOptionModel);
        viewOptionModel.setViewId(viewId);
        return Optional.ofNullable(conversionService.convert(viewOptionModel, ViewOption.class))
                .map(viewOption -> {
                    log.debug("viewOption:{}", viewOption);
                    View view = viewOption.getView();
                    if (!viewId.equals(view.getId())) {
                        throw new ResourceNotFoundException("View not found");
                    }
                    viewOption = viewOptionRepository.save(viewOption);
                    log.debug("viewOption:{}", viewOption);
                    return Optional.ofNullable(conversionService.convert(viewOption, ViewOptionModel.class))
                            .orElseThrow(() -> new MappingException("View option not converted"));
                })
                .orElseThrow(() -> new MappingException("View option model not converted"));
    }

    @Override
    public ViewOptionModel getViewOption(Long viewId, Long optionId) {
        log.debug("viewId:{},optionId:{}", viewId, optionId);
        return viewOptionRepository.findById(optionId)
                .map(viewOption -> {
                    if (!viewId.equals(viewOption.getView().getId())) {
                        throw new ResourceNotFoundException("View not found");
                    }
                    return conversionService.convert(viewOption, ViewOptionModel.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("View option not found"));
    }

    @Override
    public ViewOptionModel setViewOption(Long viewId, Long optionId, ViewOptionModel viewOptionModel) {
        log.debug("viewId:{},optionId:{},viewOptionModel:{}", viewId, optionId, viewOptionModel);
        viewOptionModel.setId(optionId);
        viewOptionModel.setViewId(viewId);
        return Optional.ofNullable(conversionService.convert(viewOptionModel, ViewOption.class))
                .map(viewOption -> {
                    log.debug("viewOption:{}", viewOption);
                    View view = viewOption.getView();
                    if (!viewId.equals(view.getId())) {
                        throw new ResourceNotFoundException("View not found");
                    }
                    viewOption = viewOptionRepository.save(viewOption);
                    log.debug("viewOption:{}", viewOption);
                    return Optional.ofNullable(conversionService.convert(viewOption, ViewOptionModel.class))
                            .orElseThrow(() -> new MappingException("View option not converted"));
                })
                .orElseThrow(() -> new MappingException("View option model not converted"));
    }

    @Override
    public void removeViewOption(Long viewId, Long optionId) {
        log.debug("viewId:{},optionId:{}", viewId, optionId);
        viewOptionRepository.findById(optionId)
                .map(viewOption -> {
                    log.debug("viewOption:{}", viewOption);
                    View view = viewOption.getView();
                    if (!viewId.equals(view.getId())) {
                        throw new ResourceNotFoundException("View not found");
                    }
                    viewOptionRepository.deleteById(optionId);
                    return viewOption;
                })
                .orElseThrow(() -> new ResourceNotFoundException("View option not found"));
    }

    @Override
    public ViewOptionModel updateViewOption(Long viewId, Long optionId, ViewOptionModel viewOptionModel) {
        log.debug("viewId:{},optionId:{},viewOptionModel:{}", viewId, optionId, viewOptionModel);
        return viewOptionRepository.findById(optionId)
                .map(viewOption -> {
                    log.debug("viewOption:{}", viewOption);
                    View view = viewOption.getView();
                    if (!viewId.equals(view.getId())) {
                        throw new ResourceNotFoundException("View not found");
                    }
                    viewOption = viewOptionModelToViewOptionMapper.update(viewOptionModel, viewOption);
                    viewOption = viewOptionRepository.save(viewOption);
                    log.debug("viewOption:{}", viewOption);
                    return conversionService.convert(viewOption, ViewOptionModel.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("View option not found"));
    }
}
