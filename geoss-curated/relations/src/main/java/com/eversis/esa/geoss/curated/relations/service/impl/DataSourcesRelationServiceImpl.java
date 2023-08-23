package com.eversis.esa.geoss.curated.relations.service.impl;

import com.eversis.esa.geoss.curated.relations.domain.RelationDataSources;
import com.eversis.esa.geoss.curated.relations.mapper.DataSourcesRelationMapper;
import com.eversis.esa.geoss.curated.relations.model.DataSourceModel;
import com.eversis.esa.geoss.curated.relations.repository.DataSourcesRelationRepository;
import com.eversis.esa.geoss.curated.relations.service.DataSourcesRelationService;
import org.springframework.stereotype.Service;

/**
 * The type Data sources relation service.
 */
@Service
public class DataSourcesRelationServiceImpl implements DataSourcesRelationService {

    private final DataSourcesRelationRepository dataSourceRepository;

    /**
     * Instantiates a new Data sources relation service.
     *
     * @param dataSourceRepository the data source repository
     */
    public DataSourcesRelationServiceImpl(DataSourcesRelationRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    @Override
    public RelationDataSources getOrCreateDataSource(DataSourceModel dataSource) {
        return dataSourceRepository.findByCode(dataSource.getCode())
                .orElseGet(() -> dataSourceRepository.save(DataSourcesRelationMapper.mapDataSource(dataSource)));
    }

}
