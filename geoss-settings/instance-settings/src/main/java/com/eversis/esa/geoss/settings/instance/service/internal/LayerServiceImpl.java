package com.eversis.esa.geoss.settings.instance.service.internal;

import com.eversis.esa.geoss.settings.instance.domain.Layer;
import com.eversis.esa.geoss.settings.instance.model.LayerModel;
import com.eversis.esa.geoss.settings.instance.repository.LayerRepository;
import com.eversis.esa.geoss.settings.instance.service.LayerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Layer service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class LayerServiceImpl implements LayerService {

    private final LayerRepository layerRepository;

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
    public Page<LayerModel> getSiteLayers(Long siteId, Pageable pageable) {
        log.debug("siteId:{},pageable:{}", siteId, pageable);
        Page<Layer> layers = layerRepository.findAllBySiteId(siteId, pageable);
        log.debug("layers:{}", layers);
        return layers.map(layer -> {
            log.trace("layer:{}", layer);
            LayerModel layerModel = conversionService.convert(layer, LayerModel.class);
            log.trace("layerModel:{}", layerModel);
            return layerModel;
        });
    }

    @Override
    public void deleteSiteLayers(Long siteId) {
        log.debug("siteId:{}", siteId);
        long deleted = layerRepository.deleteAllBySiteId(siteId);
        log.debug("deleted:{}", deleted);
    }
}
