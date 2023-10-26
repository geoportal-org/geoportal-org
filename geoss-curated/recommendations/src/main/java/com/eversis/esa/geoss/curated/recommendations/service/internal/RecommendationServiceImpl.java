package com.eversis.esa.geoss.curated.recommendations.service.internal;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.common.repository.DataSourceRepository;
import com.eversis.esa.geoss.curated.recommendations.domain.Recommendation;
import com.eversis.esa.geoss.curated.recommendations.domain.RecommendedEntity;
import com.eversis.esa.geoss.curated.recommendations.domain.RecommendedKeyword;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendationModel;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendedEntityModel;
import com.eversis.esa.geoss.curated.recommendations.repository.RecommendationRepository;
import com.eversis.esa.geoss.curated.recommendations.repository.RecommendedEntityRepository;
import com.eversis.esa.geoss.curated.recommendations.service.RecommendationService;
import com.eversis.esa.geoss.curated.recommendations.support.RecommendedEntityModelToRecommendedEntityMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.MappingException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Recommendation service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Validated
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;

    private final RecommendedEntityRepository recommendedEntityRepository;

    private final DataSourceRepository dataSourceRepository;

    private final RecommendedEntityModelToRecommendedEntityMapper recommendedEntityModelToRecommendedEntityMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

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
    public Page<RecommendationModel> findAllRecommendations(@NotNull Pageable pageable) {
        final Page<Recommendation> recommendations = recommendationRepository.findAll(pageable);
        final List<RecommendationModel> recommendationModels = recommendations.getContent().stream()
                .map(recommendation -> conversionService.convert(recommendation, RecommendationModel.class))
                .collect(Collectors.toList());
        return new PageImpl<>(recommendationModels, pageable, recommendations.getTotalElements());
    }

    @Override
    public RecommendationModel getRecommendation(long recommendationId) {
        log.debug("Get recommendation with id: {}", recommendationId);
        return recommendationRepository.findById(recommendationId)
                .map(recommendation -> {
                    log.debug("recommendation:{}", recommendation);
                    RecommendationModel recommendationModel = conversionService.convert(recommendation,
                            RecommendationModel.class);
                    log.debug("regionalSettings:{}", recommendationModel);
                    return recommendationModel;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation not found"));
    }

    @Transactional
    @Override
    public RecommendationModel createRecommendation(@NotNull RecommendationModel recommendationModel) {
        log.debug("Creating new recommendation - {}", recommendationModel);
        return Optional.ofNullable(conversionService.convert(recommendationModel, Recommendation.class))
                .map(recommendation -> {
                    log.debug("recommendation:{}", recommendation);
                    recommendation = recommendationRepository.save(recommendation);
                    log.debug("recommendation:{}", recommendation);
                    RecommendationModel recommendationResult = conversionService.convert(recommendation,
                            RecommendationModel.class);
                    log.debug("recommendationModel:{}", recommendationResult);
                    publish(recommendationResult);
                    return recommendationResult;
                })
                .orElseThrow(() -> new MappingException("Recommendation model not converted"));
    }

    @Transactional
    @Override
    public void removeRecommendation(long recommendationId) {
        log.debug("Deleting recommendation with id: {}", recommendationId);
        try {
            recommendationRepository.deleteById(recommendationId);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(
                    "Recommendation entity with id: " + recommendationId + " does not exist");
        }
        log.debug("Deleted recommendation with id: {}", recommendationId);
    }

    @Transactional
    @Override
    public void updateKeywords(long recommendationId, @NotEmpty Set<String> keywords) {
        log.debug("Updating recommendation with id: {} with new keywords: {}", recommendationId, keywords);
        final Recommendation recommendation = recommendationRepository.findById(recommendationId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Recommendation entity with id: " + recommendationId + " does not exist"));

        recommendation.clearRecommendedKeywords();
        recommendationRepository.saveAndFlush(recommendation);

        keywords.stream()
                .map(keyword -> conversionService.convert(keyword, RecommendedKeyword.class))
                .forEach(recommendation::addRecommendedKeyword);

        RecommendationModel recommendationResult = conversionService.convert(recommendation, RecommendationModel.class);
        log.debug("recommendationModel:{}", recommendationResult);
        publish(recommendationResult);
        log.debug("Updated recommendation with id: {} with new keywords: {}", recommendationId, keywords);
    }

    @Transactional
    @Override
    public void addEntities(long recommendationId, @NotEmpty Set<RecommendedEntityModel> recommendedEntities) {
        log.debug("Adding new entities to recommendation with id: {}, entities: {}", recommendationId,
                recommendedEntities);
        final Recommendation recommendation = recommendationRepository.findById(recommendationId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Recommendation entity with id: " + recommendationId + " does not exist"));

        final List<RecommendedEntity> entities = recommendedEntities.stream()
                .map(recommendedEntityModel -> conversionService.convert(recommendedEntityModel,
                        RecommendedEntity.class))
                .filter(entity -> !recommendedEntityRepository.existsByCodeAndDataSourceAndRecommendation(
                        entity.getCode(), entity.getDataSource(), recommendation))
                .collect(Collectors.toList());
        entities.forEach(recommendation::addRecommendedEntity);

        RecommendationModel recommendationResult = conversionService.convert(recommendation, RecommendationModel.class);
        log.debug("recommendationModel:{}", recommendationResult);
        publish(recommendationResult);
        log.debug("Updated recommendation with id: {} with new entities: {}", recommendationId, entities);
    }

    @Transactional
    @Override
    public void updateEntity(long recommendationId, RecommendedEntityModel recommendedEntityModel) {
        log.debug("Updating recommendation with id: {} and its entity: {}", recommendationId, recommendedEntityModel);
        final Recommendation recommendation = recommendationRepository.findById(recommendationId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Recommendation entity with id: " + recommendationId + " does not exist"));
        final DataSource dataSource = dataSourceRepository.findDataSourceByCode(
                recommendedEntityModel.getDataSourceCode());

        final RecommendedEntity entity = recommendedEntityRepository
                .findByCodeAndDataSourceAndRecommendation(
                        recommendedEntityModel.getEntityCode(),
                        dataSource,
                        recommendation)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recommendation entity: [" + recommendedEntityModel + "] does not exist"));
        entity.setName(recommendedEntityModel.getName());
        entity.setOrderWeight(recommendedEntityModel.getOrderWeight());

        RecommendationModel recommendationResult = conversionService.convert(recommendation, RecommendationModel.class);
        log.debug("recommendationModel:{}", recommendationResult);
        publish(recommendationResult);
        log.debug("Updated recommendation with id: {} and its entity: {}", recommendationId, entity);
    }

    @Transactional
    @Override
    public void updateEntity(long recommendationId, long recommendedEntityId,
            RecommendedEntityModel recommendedEntityModel) {
        recommendedEntityRepository.findById(recommendedEntityId)
                .map(recommendedEntity -> {
                    log.debug("recommendedEntity:{}", recommendedEntity);
                    Recommendation recommendation = recommendedEntity.getRecommendation();
                    if (!recommendation.getId().equals(recommendationId)) {
                        throw new ResourceNotFoundException("Recommendation not found");
                    }
                    recommendedEntity = recommendedEntityModelToRecommendedEntityMapper.update(recommendedEntityModel,
                            recommendedEntity);
                    recommendedEntity = recommendedEntityRepository.save(recommendedEntity);
                    log.debug("recommendedEntity:{}", recommendedEntity);

                    RecommendationModel recommendationResult = conversionService.convert(recommendation,
                            RecommendationModel.class);
                    log.debug("recommendationModel:{}", recommendationResult);
                    publish(recommendationResult);
                    return recommendedEntity;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Recommended entity not found"));
    }

    @Transactional
    @Override
    public void removeEntity(long recommendationId, @NotBlank String dataSourceCode, @NotBlank String entityCode) {
        log.debug("Removing entity from recommendation with id: {}, entity: [dataSource: {}, entityCode: {}]",
                recommendationId, dataSourceCode, entityCode);

        final Recommendation recommendation = recommendationRepository.findById(recommendationId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Recommendation entity with id: " + recommendationId + " does not exist"));
        final DataSource dataSource = dataSourceRepository.findDataSourceByCode(dataSourceCode);
        final RecommendedEntity entity = recommendedEntityRepository
                .findByCodeAndDataSourceAndRecommendation(
                        entityCode,
                        dataSource,
                        recommendation)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Recommendation entity: [dataSource: " + dataSourceCode + ", entityCode: " + entityCode
                        + "] does not exist"));

        recommendedEntityRepository.delete(entity);

        RecommendationModel recommendationResult = conversionService.convert(recommendation, RecommendationModel.class);
        log.debug("recommendationModel:{}", recommendationResult);
        publish(recommendationResult);
        log.debug("Deleted entity {} from recommendation with id: {}", recommendationId, entity);
    }

    @Transactional
    @Override
    public void removeEntity(long recommendationId, long recommendedEntityId) {
        recommendedEntityRepository.findById(recommendedEntityId)
                .map(recommendedEntity -> {
                    log.debug("recommendedEntity:{}", recommendedEntity);
                    Recommendation recommendation = recommendedEntity.getRecommendation();
                    if (!recommendation.getId().equals(recommendationId)) {
                        throw new ResourceNotFoundException("Recommendation not found");
                    }
                    recommendedEntityRepository.delete(recommendedEntity);

                    RecommendationModel recommendationResult = conversionService.convert(recommendation,
                            RecommendationModel.class);
                    log.debug("recommendationModel:{}", recommendationResult);
                    publish(recommendationResult);
                    return recommendedEntity;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Recommended entity not found"));
    }

    @Override
    public List<String> getDataSourcesCodes() {
        return dataSourceRepository.findAll().stream()
                .map(DataSource::getCode)
                .toList();
    }

    private void publish(RecommendationModel recommendationModel) {
        if (recommendationModel != null) {
            applicationEventPublisher.publishEvent(recommendationModel);
        }
    }
}
