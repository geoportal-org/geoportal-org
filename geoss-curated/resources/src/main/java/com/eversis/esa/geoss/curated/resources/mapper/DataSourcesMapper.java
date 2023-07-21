package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.resources.domain.DataSources;
import com.eversis.esa.geoss.curated.resources.model.DataSourceModel;

/**
 * The type Data source mapper.
 */
public class DataSourcesMapper {

    private DataSourcesMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map data source data source.
     *
     * @param domainDataSource the domain data source
     * @return the data source
     */
    public static DataSources mapDataSource(DataSourceModel domainDataSource) {
        DataSources dbDataSources = new DataSources(domainDataSource.getCode());
        dbDataSources.setName(domainDataSource.getName());
        dbDataSources.setIsCustom(1);
        return dbDataSources;
    }

}
