package com.eversis.esa.geoss.curated.recommendations.support;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.common.repository.DataSourceRepository;
import com.eversis.esa.geoss.curated.recommendations.domain.RecommendedEntity;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendedEntityModel;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The interface Recommended entity model to recommended entity mapper.
 */
@Mapper(componentModel = ComponentModel.SPRING, implementationPackage = "<PACKAGE_NAME>.internal")
public abstract class RecommendedEntityModelToRecommendedEntityMapper {

    private DataSourceRepository dataSourceRepository;

    /**
     * Sets data source repository.
     *
     * @param dataSourceRepository the data source repository
     */
    @Autowired
    public void setDataSourceRepository(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    /**
     * Update recommended entity model.
     *
     * @param recommendedEntityModel the recommended entity model
     * @param recommendedEntity the recommended entity
     * @return the recommended entity model
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "recommendation", ignore = true)
    @Mapping(target = "dataSource", source = "dataSourceCode")
    @Mapping(target = "code", source = "entityCode")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    public abstract RecommendedEntity update(RecommendedEntityModel recommendedEntityModel,
            @MappingTarget RecommendedEntity recommendedEntity);

    protected DataSource map(String value) {
        return dataSourceRepository.findDataSourceByCode(value);
    }
}
