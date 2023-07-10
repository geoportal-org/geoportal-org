package com.eversis.esa.geoss.curated.recommendations.support;

import com.eversis.esa.geoss.curated.recommendations.domain.DataSource;
import com.eversis.esa.geoss.curated.recommendations.domain.RecommendedEntity;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendedEntityModel;
import com.eversis.esa.geoss.curated.recommendations.repository.DataSourceRepository;
import com.eversis.esa.geoss.curated.recommendations.repository.RecommendedEntityRepository;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * The interface Recommended entity model to recommended entity converter.
 */
@Mapper(componentModel = ComponentModel.SPRING, implementationPackage = "<PACKAGE_NAME>.internal")
public abstract class RecommendedEntityModelToRecommendedEntityConverter implements
        Converter<RecommendedEntityModel, RecommendedEntity> {

    private RecommendedEntityRepository recommendedEntityRepository;

    private DataSourceRepository dataSourceRepository;

    /**
     * Sets view option repository.
     *
     * @param recommendedEntityRepository the recommended entity repository
     */
    @Autowired
    public void setViewOptionRepository(RecommendedEntityRepository recommendedEntityRepository) {
        this.recommendedEntityRepository = recommendedEntityRepository;
    }

    /**
     * Sets data source repository.
     *
     * @param dataSourceRepository the data source repository
     */
    @Autowired
    public void setDataSourceRepository(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "recommendation", ignore = true)
    @Mapping(target = "dataSource", source = "dataSourceCode")
    @Mapping(target = "code", source = "entityCode")
    @Override
    public abstract RecommendedEntity convert(RecommendedEntityModel source);

    @ObjectFactory
    protected RecommendedEntity createRecommendedEntity(RecommendedEntityModel recommendedEntityModel) {
        if (recommendedEntityModel.getId() == null) {
            return new RecommendedEntity();
        }
        return recommendedEntityRepository.getReferenceById(recommendedEntityModel.getId());
    }

    protected DataSource map(String value) {
        return dataSourceRepository.findDataSourceByCode(value);
    }
}
