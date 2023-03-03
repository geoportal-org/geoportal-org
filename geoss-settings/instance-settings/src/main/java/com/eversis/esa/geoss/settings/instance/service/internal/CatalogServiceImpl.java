package com.eversis.esa.geoss.settings.instance.service.internal;

import com.eversis.esa.geoss.settings.instance.domain.Catalog;
import com.eversis.esa.geoss.settings.instance.domain.CatalogOption;
import com.eversis.esa.geoss.settings.instance.model.CatalogOptionModel;
import com.eversis.esa.geoss.settings.instance.repository.CatalogOptionRepository;
import com.eversis.esa.geoss.settings.instance.service.CatalogService;
import com.eversis.esa.geoss.settings.instance.support.CatalogOptionModelToCatalogOptionMapper;

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
 * The type Catalog service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class CatalogServiceImpl implements CatalogService {

    private final CatalogOptionRepository catalogOptionRepository;

    private final CatalogOptionModelToCatalogOptionMapper catalogOptionModelToCatalogOptionMapper;

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
    public List<CatalogOptionModel> getCatalogOptions(Long catalogId) {
        log.debug("catalogId:{}", catalogId);
        return catalogOptionRepository.findByCatalogId(catalogId)
                .map(catalogOption -> conversionService.convert(catalogOption, CatalogOptionModel.class))
                .toList();
    }

    @Override
    public void removeCatalogOptions(Long catalogId) {
        log.debug("catalogId:{}", catalogId);
        long count = catalogOptionRepository.deleteAllByCatalogId(catalogId);
        log.debug("count:{}", count);
    }

    @Override
    public CatalogOptionModel addCatalogOption(Long catalogId, CatalogOptionModel catalogOptionModel) {
        log.debug("catalogId:{},catalogOptionModel:{}", catalogId, catalogOptionModel);
        catalogOptionModel.setCatalogId(catalogId);
        return Optional.ofNullable(conversionService.convert(catalogOptionModel, CatalogOption.class))
                .map(catalogOption -> {
                    log.debug("catalogOption:{}", catalogOption);
                    Catalog catalog = catalogOption.getCatalog();
                    if (!catalogId.equals(catalog.getId())) {
                        throw new ResourceNotFoundException("Catalog not found");
                    }
                    catalogOption = catalogOptionRepository.save(catalogOption);
                    log.debug("catalogOption:{}", catalogOption);
                    return Optional.ofNullable(conversionService.convert(catalogOption, CatalogOptionModel.class))
                            .orElseThrow(() -> new MappingException("Catalog option not converted"));
                })
                .orElseThrow(() -> new MappingException("Catalog option model not converted"));
    }

    @Override
    public CatalogOptionModel getCatalogOption(Long catalogId, Long optionId) {
        log.debug("catalogId:{},optionId:{}", catalogId, optionId);
        return catalogOptionRepository.findById(optionId)
                .map(catalogOption -> {
                    if (!catalogId.equals(catalogOption.getCatalog().getId())) {
                        throw new ResourceNotFoundException("Catalog not found");
                    }
                    return conversionService.convert(catalogOption, CatalogOptionModel.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Catalog option not found"));
    }

    @Override
    public CatalogOptionModel setCatalogOption(Long catalogId, Long optionId, CatalogOptionModel catalogOptionModel) {
        log.debug("catalogId:{},optionId:{},catalogOptionModel:{}", catalogId, optionId, catalogOptionModel);
        catalogOptionModel.setId(optionId);
        catalogOptionModel.setCatalogId(catalogId);
        return Optional.ofNullable(conversionService.convert(catalogOptionModel, CatalogOption.class))
                .map(catalogOption -> {
                    log.debug("catalogOption:{}", catalogOption);
                    Catalog catalog = catalogOption.getCatalog();
                    if (!catalogId.equals(catalog.getId())) {
                        throw new ResourceNotFoundException("Catalog not found");
                    }
                    catalogOption = catalogOptionRepository.save(catalogOption);
                    log.debug("catalogOption:{}", catalogOption);
                    return Optional.ofNullable(conversionService.convert(catalogOption, CatalogOptionModel.class))
                            .orElseThrow(() -> new MappingException("Catalog option not converted"));
                })
                .orElseThrow(() -> new MappingException("Catalog option model not converted"));
    }

    @Override
    public void removeCatalogOption(Long catalogId, Long optionId) {
        log.debug("catalogId:{},optionId:{}", catalogId, optionId);
        catalogOptionRepository.findById(optionId)
                .map(catalogOption -> {
                    log.debug("catalogOption:{}", catalogOption);
                    Catalog catalog = catalogOption.getCatalog();
                    if (!catalogId.equals(catalog.getId())) {
                        throw new ResourceNotFoundException("Catalog not found");
                    }
                    catalogOptionRepository.deleteById(optionId);
                    return catalogOption;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Catalog option not found"));
    }

    @Override
    public CatalogOptionModel updateCatalogOption(Long catalogId, Long optionId,
            CatalogOptionModel catalogOptionModel) {
        log.debug("catalogId:{},optionId:{},catalogOptionModel:{}", catalogId, optionId, catalogOptionModel);
        return catalogOptionRepository.findById(optionId)
                .map(catalogOption -> {
                    log.debug("catalogOption:{}", catalogOption);
                    Catalog catalog = catalogOption.getCatalog();
                    if (!catalogId.equals(catalog.getId())) {
                        throw new ResourceNotFoundException("Catalog not found");
                    }
                    catalogOption = catalogOptionModelToCatalogOptionMapper.update(catalogOptionModel, catalogOption);
                    catalogOption = catalogOptionRepository.save(catalogOption);
                    log.debug("catalogOption:{}", catalogOption);
                    return conversionService.convert(catalogOption, CatalogOptionModel.class);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Catalog option not found"));
    }
}
