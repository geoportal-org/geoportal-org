package com.eversis.esa.geoss.curated.common.service.impl;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.common.mapper.DataSourceMapper;
import com.eversis.esa.geoss.curated.common.model.DataSourceModel;
import com.eversis.esa.geoss.curated.common.repository.DataSourceRepository;
import com.eversis.esa.geoss.curated.common.service.DataSourceService;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Data source service.
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

    private final DataSourceRepository dataSourceRepository;

    /**
     * Instantiates a new Data source service.
     *
     * @param dataSourceRepository the data source repository
     */
    public DataSourceServiceImpl(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    @Override
    public DataSource getOrCreateDataSource(DataSourceModel dataSource) {
        return dataSourceRepository.findByCode(dataSource.getCode())
                .orElseGet(() -> dataSourceRepository.save(DataSourceMapper.mapDataSource(dataSource)));
    }

    @Override
    public List<DataSource> getDataSources() {
        return dataSourceRepository.findAll();
    }

    @Override
    public List<String> getDataSourcesCodes() {
        return dataSourceRepository.findAll().stream()
                .map(DataSource::getCode)
                .toList();
    }
}
