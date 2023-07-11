package com.eversis.esa.geoss.curated.recommendations.service;

import com.eversis.esa.geoss.curated.recommendations.domain.DataSource;
import com.eversis.esa.geoss.curated.recommendations.domain.Recommendation;
import com.eversis.esa.geoss.curated.recommendations.domain.RecommendedEntity;
import com.eversis.esa.geoss.curated.recommendations.domain.RecommendedKeyword;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendationModel;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendedEntityModel;
import com.eversis.esa.geoss.curated.recommendations.repository.DataSourceRepository;
import com.eversis.esa.geoss.curated.recommendations.repository.RecommendationRepository;
import com.eversis.esa.geoss.curated.recommendations.repository.RecommendedEntityRepository;
import com.eversis.esa.geoss.curated.recommendations.service.internal.RecommendationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import jakarta.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * The type Recommendation service tests.
 */
@ExtendWith(MockitoExtension.class)
class RecommendationServiceTests {

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private RecommendedEntityRepository recommendedEntityRepository;

    @Mock
    private DataSourceRepository dataSourceRepository;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    @BeforeEach
    public void setUp() throws Exception {
        recommendationService.setConversionService(conversionService);
    }

    /**
     * When no entities are provided recommendation is not created.
     */
    @Test
    void whenNoEntitiesAreProvided_recommendationIsNotCreated() {
        RecommendationModel recommendationModel = new RecommendationModel();
        recommendationModel.setEntities(Collections.emptySet());
        recommendationModel.setKeywords(createDummyKeywords());

        assertThrows(ConstraintViolationException.class, () -> {
            recommendationService.createRecommendation(recommendationModel);
        });
    }

    /**
     * When no keywords are provided recommendation is not created.
     */
    @Test
    void whenNoKeywordsAreProvided_recommendationIsNotCreated() {
        RecommendationModel recommendationModel = new RecommendationModel();
        Set<RecommendedEntityModel> entities = new HashSet<>();
        entities.add(createDummyRecommendedEntityModel());
        recommendationModel.setEntities(entities);
        recommendationModel.setKeywords(Collections.emptySet());

        assertThrows(ConstraintViolationException.class, () -> {
            recommendationService.createRecommendation(recommendationModel);
        });
    }

    /**
     * When recommendation is created no exception is thrown.
     */
    @Test
    void whenRecommendationIsCreated_NoExceptionIsThrown() {
        RecommendationModel recommendationModel = new RecommendationModel();
        Set<RecommendedEntityModel> entities = new HashSet<>();
        entities.add(createDummyRecommendedEntityModel());
        recommendationModel.setEntities(entities);
        Set<String> keywords = createDummyKeywords();
        recommendationModel.setKeywords(keywords);

        when(recommendationRepository.save(any(Recommendation.class))).thenReturn(createDummyRecommendation());
        for (RecommendedEntityModel recommendedEntityModel : entities) {
            when(conversionService.convert(recommendedEntityModel, RecommendedEntity.class)).thenReturn(
                    map(recommendedEntityModel));
        }
        for (String keyword : keywords) {
            when(conversionService.convert(keyword, RecommendedKeyword.class)).thenReturn(map(keyword));
        }

        assertDoesNotThrow(() -> recommendationService.createRecommendation(recommendationModel));
    }

    /**
     * When deleting existing recommendation no exception is thrown.
     */
    @Test
    void whenDeletingExistingRecommendation_noExceptionIsThrown() {
        long recommendationId = 5L;

        assertDoesNotThrow(() -> recommendationService.removeRecommendation(recommendationId));
    }

    /**
     * When recommendation not exists during removal exception is thrown.
     */
    @Test
    void whenRecommendationNotExistsDuringRemoval_ExceptionIsThrown() {
        long recommendationId = 5L;

        doThrow(EmptyResultDataAccessException.class).when(recommendationRepository).deleteById(recommendationId);

        assertThrows(ResourceNotFoundException.class,
                () -> recommendationService.removeRecommendation(recommendationId));
    }

    /**
     * When recommendation not exists during keywords update exception is thrown.
     */
    @Test
    void whenRecommendationNotExistsDuringKeywordsUpdate_ExceptionIsThrown() {
        long recommendationId = 5L;
        Set<String> keywords = createDummyKeywords();

        when(recommendationRepository.findById(recommendationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> recommendationService.updateKeywords(recommendationId, keywords));
    }

    /**
     * When updating keywords of existing recommendation no exception is thrown.
     */
    @Test
    void whenUpdatingKeywordsOfExistingRecommendation_noExceptionIsThrown() {
        long recommendationId = 5L;
        Set<String> keywords = createDummyKeywords();

        when(recommendationRepository.findById(recommendationId)).thenReturn(Optional.of(createDummyRecommendation()));
        for (String keyword : keywords) {
            when(conversionService.convert(keyword, RecommendedKeyword.class)).thenReturn(map(keyword));
        }

        assertDoesNotThrow(() -> recommendationService.updateKeywords(recommendationId, keywords));
    }

    /**
     * When recommendation not exists during adding entities exception is thrown.
     */
    @Test
    void whenRecommendationNotExistsDuringAddingEntities_ExceptionIsThrown() {
        long recommendationId = 5L;
        Set<RecommendedEntityModel> entities = Stream.of(createDummyRecommendedEntityModel(),
                        createDummyRecommendedEntityModel())
                .collect(Collectors.toSet());

        when(recommendationRepository.findById(recommendationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> recommendationService.addEntities(recommendationId, entities));
    }

    /**
     * When adding entities of existing recommendation no exception is thrown.
     */
    @Test
    void whenAddingEntitiesOfExistingRecommendation_noExceptionIsThrown() {
        long recommendationId = 5L;
        Set<RecommendedEntityModel> entities = Stream.of(createDummyRecommendedEntityModel(),
                        createDummyRecommendedEntityModel())
                .collect(Collectors.toSet());

        when(recommendationRepository.findById(recommendationId)).thenReturn(Optional.of(createDummyRecommendation()));
        for (RecommendedEntityModel recommendedEntityModel : entities) {
            when(conversionService.convert(recommendedEntityModel, RecommendedEntity.class)).thenReturn(
                    map(recommendedEntityModel));
        }

        assertDoesNotThrow(() -> recommendationService.addEntities(recommendationId, entities));
    }

    /**
     * When recommendation not exists during entity update exception is thrown.
     */
    @Test
    void whenRecommendationNotExistsDuringEntityUpdate_ExceptionIsThrown() {
        long recommendationId = 5L;
        RecommendedEntityModel recommendedEntityModel = createDummyRecommendedEntityModel();

        when(recommendationRepository.findById(recommendationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> recommendationService.updateEntity(recommendationId, recommendedEntityModel));
    }

    /**
     * When updating entity of existing recommendation no exception is thrown.
     */
    @Test
    void whenUpdatingEntityOfExistingRecommendation_noExceptionIsThrown() {
        long recommendationId = 5L;
        RecommendedEntityModel recommendedEntityModel = createDummyRecommendedEntityModel();
        Recommendation recommendation = createDummyRecommendation();
        DataSource dataSource = new DataSource();

        when(recommendationRepository.findById(recommendationId)).thenReturn(Optional.of(recommendation));
        when(dataSourceRepository.findDataSourceByCode(recommendedEntityModel.getDataSourceCode())).thenReturn(
                dataSource);
        when(recommendedEntityRepository.findByCodeAndDataSourceAndRecommendation(
                recommendedEntityModel.getEntityCode(), dataSource, recommendation))
                .thenReturn(Optional.of(createDummyRecommendedEntity()));

        assertDoesNotThrow(() -> recommendationService.updateEntity(recommendationId, recommendedEntityModel));
    }

    /**
     * When updating non existing entity exception is thrown.
     */
    @Test
    void whenUpdatingNonExistingEntity_exceptionIsThrown() {
        long recommendationId = 5L;
        RecommendedEntityModel recommendedEntityModel = createDummyRecommendedEntityModel();
        Recommendation recommendation = createDummyRecommendation();
        DataSource dataSource = new DataSource();

        when(recommendationRepository.findById(recommendationId)).thenReturn(Optional.of(recommendation));
        when(dataSourceRepository.findDataSourceByCode(recommendedEntityModel.getDataSourceCode())).thenReturn(
                dataSource);
        when(recommendedEntityRepository.findByCodeAndDataSourceAndRecommendation(
                recommendedEntityModel.getEntityCode(), dataSource, recommendation))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> recommendationService.updateEntity(recommendationId, recommendedEntityModel));
    }

    /**
     * When recommendation not exists during entity removal exception is thrown.
     */
    @Test
    void whenRecommendationNotExistsDuringEntityRemoval_exceptionIsThrown() {
        long recommendationId = 5L;
        RecommendedEntityModel recommendedEntityModel = createDummyRecommendedEntityModel();

        when(recommendationRepository.findById(recommendationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> recommendationService.removeEntity(recommendationId, recommendedEntityModel.getDataSourceCode(),
                        recommendedEntityModel.getEntityCode()));
    }

    /**
     * When removing entity of existing recommendation no exception is thrown.
     */
    @Test
    void whenRemovingEntityOfExistingRecommendation_noExceptionIsThrown() {
        long recommendationId = 5L;
        RecommendedEntityModel recommendedEntityModel = createDummyRecommendedEntityModel();
        Recommendation recommendation = createDummyRecommendation();
        DataSource dataSource = new DataSource();

        when(recommendationRepository.findById(recommendationId)).thenReturn(Optional.of(recommendation));
        when(dataSourceRepository.findDataSourceByCode(recommendedEntityModel.getDataSourceCode())).thenReturn(
                dataSource);
        when(recommendedEntityRepository.findByCodeAndDataSourceAndRecommendation(
                recommendedEntityModel.getEntityCode(), dataSource, recommendation))
                .thenReturn(Optional.of(createDummyRecommendedEntity()));

        assertDoesNotThrow(
                () -> recommendationService.removeEntity(recommendationId, recommendedEntityModel.getDataSourceCode(),
                        recommendedEntityModel.getEntityCode()));
    }

    /**
     * When removing non existing entity exception is thrown.
     */
    @Test
    void whenRemovingNonExistingEntity_exceptionIsThrown() {
        long recommendationId = 5L;
        RecommendedEntityModel recommendedEntityModel = createDummyRecommendedEntityModel();
        Recommendation recommendation = createDummyRecommendation();
        DataSource dataSource = new DataSource();

        when(recommendationRepository.findById(recommendationId)).thenReturn(Optional.of(recommendation));
        when(dataSourceRepository.findDataSourceByCode(recommendedEntityModel.getDataSourceCode())).thenReturn(
                dataSource);
        when(recommendedEntityRepository.findByCodeAndDataSourceAndRecommendation(
                recommendedEntityModel.getEntityCode(), dataSource, recommendation))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> recommendationService.removeEntity(recommendationId, recommendedEntityModel.getDataSourceCode(),
                        recommendedEntityModel.getEntityCode()));
    }

    private Recommendation createDummyRecommendation() {
        return new Recommendation();
    }

    private RecommendedEntity createDummyRecommendedEntity() {
        return new RecommendedEntity();
    }

    private RecommendedEntityModel createDummyRecommendedEntityModel() {
        RecommendedEntityModel recommendedEntityModel = new RecommendedEntityModel();
        recommendedEntityModel.setDataSourceCode("dab");
        recommendedEntityModel.setEntityCode(UUID.randomUUID().toString());
        recommendedEntityModel.setName("Entity name");
        return recommendedEntityModel;
    }

    private Set<String> createDummyKeywords() {
        return Stream.of("keyword1", "keyword2", "keyword3").collect(Collectors.toSet());
    }

    private RecommendedEntity map(RecommendedEntityModel recommendedEntityModel) {
        RecommendedEntity recommendedEntity = new RecommendedEntity();
        recommendedEntity.setName(recommendedEntityModel.getName());
        recommendedEntity.setCode(recommendedEntityModel.getEntityCode());
        DataSource dataSource = new DataSource();
        dataSource.setCode(recommendedEntityModel.getDataSourceCode());
        recommendedEntity.setDataSource(dataSource);
        return recommendedEntity;
    }

    private RecommendedKeyword map(String keyword) {
        RecommendedKeyword recommendedKeyword = new RecommendedKeyword();
        recommendedKeyword.setName(keyword);
        return recommendedKeyword;
    }
}
