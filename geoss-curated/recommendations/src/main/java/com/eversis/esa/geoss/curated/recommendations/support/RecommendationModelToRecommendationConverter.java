package com.eversis.esa.geoss.curated.recommendations.support;

import com.eversis.esa.geoss.curated.recommendations.domain.Recommendation;
import com.eversis.esa.geoss.curated.recommendations.domain.RecommendedEntity;
import com.eversis.esa.geoss.curated.recommendations.domain.RecommendedKeyword;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendationModel;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendedEntityModel;
import com.eversis.esa.geoss.curated.recommendations.repository.RecommendationRepository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.Set;

/**
 * The type Recommendation model to recommendation converter.
 */
@Mapper(componentModel = ComponentModel.SPRING, implementationPackage = "<PACKAGE_NAME>.internal")
public abstract class RecommendationModelToRecommendationConverter implements
        Converter<RecommendationModel, Recommendation> {

    private RecommendationRepository recommendationRepository;

    private ConversionService conversionService;

    /**
     * Sets recommendation repository.
     *
     * @param recommendationRepository the recommendation repository
     */
    @Autowired
    public void setRecommendationRepository(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    /**
     * Sets conversion service.
     *
     * @param conversionService the conversion service
     */
    @Lazy
    @Autowired
    public void setConversionService(@Qualifier("mvcConversionService") ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    @Override
    public abstract Recommendation convert(RecommendationModel source);

    @ObjectFactory
    protected Recommendation createRecommendation(RecommendationModel recommendationModel) {
        if (recommendationModel.getId() == null) {
            return new Recommendation();
        }
        return recommendationRepository.getReferenceById(recommendationModel.getId());
    }

    protected RecommendedEntity recommendedEntityModelToRecommendedEntity(
            RecommendedEntityModel recommendedEntityModel) {
        return conversionService.convert(recommendedEntityModel, RecommendedEntity.class);
    }

    protected List<RecommendedKeyword> map(Set<String> value) {
        return value.stream().map(s -> {
            RecommendedKeyword recommendedKeyword = new RecommendedKeyword();
            recommendedKeyword.setName(s);
            return recommendedKeyword;
        }).toList();
    }
}
