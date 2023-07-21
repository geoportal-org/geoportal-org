package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.DataSources;
import com.eversis.esa.geoss.curated.resources.mapper.DataSourcesMapper;
import com.eversis.esa.geoss.curated.resources.model.DataSourceModel;
import com.eversis.esa.geoss.curated.resources.repository.DataSourcesRepository;
import com.eversis.esa.geoss.curated.resources.service.DataSourcesService;
import org.springframework.stereotype.Service;

/**
 * The type Data source service.
 */
@Service
public class DataSourcesServiceImpl implements DataSourcesService {

    private final DataSourcesRepository dataSourceRepository;

    /**
     * Instantiates a new Data source service.
     *
     * @param dataSourceRepository the data source repository
     */
    public DataSourcesServiceImpl(DataSourcesRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    @Override
    public DataSources getOrCreateDataSource(DataSourceModel dataSource) {
        return dataSourceRepository.findByCode(dataSource.getCode())
                .orElseGet(() -> dataSourceRepository.save(DataSourcesMapper.mapDataSource(dataSource)));
    }

}
