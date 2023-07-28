package com.eversis.esa.geoss.curated.recommendations.support;

import com.eversis.esa.geoss.curated.recommendations.domain.Recommendation;
import com.eversis.esa.geoss.curated.recommendations.domain.RecommendedEntity;
import com.eversis.esa.geoss.curated.recommendations.domain.RecommendedKeyword;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendationModel;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendedEntityModel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The interface Recommendation to recommendation model converter.
 */
@Mapper(componentModel = ComponentModel.SPRING, implementationPackage = "<PACKAGE_NAME>.internal")
public interface RecommendationToRecommendationModelConverter extends Converter<Recommendation, RecommendationModel> {

    @Override
    RecommendationModel convert(Recommendation source);

    /**
     * Recommended entity to recommended entity model recommended entity model.
     *
     * @param recommendedEntity the recommended entity
     * @return the recommended entity model
     */
    @Mapping(target = "dataSourceCode", source = "dataSource.code")
    @Mapping(target = "entityCode", source = "code")
    RecommendedEntityModel recommendedEntityToRecommendedEntityModel(RecommendedEntity recommendedEntity);

    /**
     * Map set.
     *
     * @param value the value
     * @return the set
     */
    default Set<String> map(List<RecommendedKeyword> value) {
        return value.stream().map(RecommendedKeyword::getName).collect(Collectors.toSet());
    }
}
