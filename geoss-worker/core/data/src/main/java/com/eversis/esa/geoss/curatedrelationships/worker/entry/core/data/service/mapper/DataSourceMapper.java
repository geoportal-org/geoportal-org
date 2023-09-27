package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.DataSource;

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
    public static DataSource mapDataSource(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DataSource domainDataSource) {
        DataSource dbDataSource = new DataSource(domainDataSource.getCode());
        dbDataSource.setName(domainDataSource.getName());
        return dbDataSource;
    }

}
