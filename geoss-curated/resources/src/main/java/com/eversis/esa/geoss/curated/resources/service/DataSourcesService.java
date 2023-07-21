package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.DataSources;
import com.eversis.esa.geoss.curated.resources.model.DataSourceModel;

/**
 * The interface Data sources service.
 */
public interface DataSourcesService {

    /**
     * Gets or create data source.
     *
     * @param dataSource the data source
     * @return the or create data source
     */
    DataSources getOrCreateDataSource(DataSourceModel dataSource);

}
