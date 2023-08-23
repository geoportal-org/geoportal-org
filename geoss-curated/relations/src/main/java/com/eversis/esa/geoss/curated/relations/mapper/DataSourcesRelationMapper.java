package com.eversis.esa.geoss.curated.relations.mapper;

import com.eversis.esa.geoss.curated.relations.domain.RelationDataSources;
import com.eversis.esa.geoss.curated.relations.model.DataSourceModel;

/**
 * The type Data source mapper.
 */
public class DataSourcesRelationMapper {

    private DataSourcesRelationMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map data source data source.
     *
     * @param domainDataSource the domain data source
     * @return the data source
     */
    public static RelationDataSources mapDataSource(DataSourceModel domainDataSource) {
        RelationDataSources dbDataSources = new RelationDataSources(domainDataSource.getCode());
        dbDataSources.setName(domainDataSource.getName());
        dbDataSources.setIsCustom(1);
        return dbDataSources;
    }

}
