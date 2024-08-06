package com.eversis.esa.geoss.curated.common.service;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.common.model.DataSourceModel;

import java.util.List;

/**
 * The interface Data sources service.
 */
public interface DataSourceService {

    /**
     * Gets or create data source.
     *
     * @param dataSource the data source
     * @return the or create data source
     */
    DataSource getOrCreateDataSource(DataSourceModel dataSource);

    /**
     * Gets data sources.
     *
     * @return the data sources
     */
    List<DataSource> getDataSources();

    /**
     * Gets data sources codes.
     *
     * @return the data sources codes
     */
    List<String> getDataSourcesCodes();
}
