package com.eversis.esa.geoss.curated.relations.service;

import com.eversis.esa.geoss.curated.relations.domain.RelationDataSources;
import com.eversis.esa.geoss.curated.relations.model.DataSourceModel;

/**
 * The interface Data sources relation service.
 */
public interface DataSourcesRelationService {

    /**
     * Gets or create data source.
     *
     * @param dataSource the data source
     * @return the or create data source
     */
    RelationDataSources getOrCreateDataSource(DataSourceModel dataSource);

}
