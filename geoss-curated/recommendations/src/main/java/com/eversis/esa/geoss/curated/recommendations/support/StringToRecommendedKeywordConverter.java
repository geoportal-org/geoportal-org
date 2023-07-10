package com.eversis.esa.geoss.curated.recommendations.support;

import com.eversis.esa.geoss.curated.recommendations.domain.RecommendedKeyword;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;
import org.springframework.core.convert.converter.Converter;

/**
 * The interface String to recommended keyword converter.
 */
@Mapper(componentModel = ComponentModel.SPRING, implementationPackage = "<PACKAGE_NAME>.internal")
public abstract class StringToRecommendedKeywordConverter implements Converter<String, RecommendedKeyword> {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Override
    public abstract RecommendedKeyword convert(String source);

    @ObjectFactory
    protected RecommendedKeyword createRecommendedKeyword(String source) {
        RecommendedKeyword recommendedKeyword = new RecommendedKeyword();
        recommendedKeyword.setName(source);
        return recommendedKeyword;
    }
}
