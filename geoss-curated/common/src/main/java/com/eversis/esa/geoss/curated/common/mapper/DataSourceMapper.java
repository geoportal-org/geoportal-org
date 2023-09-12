package com.eversis.esa.geoss.curated.common.mapper;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.common.model.DataSourceModel;

/**
 * The type Data source mapper.
 */
public class DataSourceMapper {

    private DataSourceMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map data source data source.
     *
     * @param domainDataSource the domain data source
     * @return the data source
     */
    public static DataSource mapDataSource(DataSourceModel domainDataSource) {
        DataSource dbDataSource = new DataSource(domainDataSource.getCode());
        dbDataSource.setName(domainDataSource.getName());
        dbDataSource.setIsCustom(1);
        return dbDataSource;
    }

}
