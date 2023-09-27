package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository.DataSourceRepository;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper.DataSourceMapper;

import org.springframework.stereotype.Service;

/**
 * The type Data source service.
 */
@Service
public class DataSourceService {

    private final DataSourceRepository dataSourceRepository;

    /**
     * Instantiates a new Data source service.
     *
     * @param dataSourceRepository the data source repository
     */
    public DataSourceService(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    /**
     * Gets or create data source.
     *
     * @param dataSource the data source
     * @return the or create data source
     */
    public DataSource getOrCreateDataSource(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DataSource dataSource) {
        return dataSourceRepository.findByCode(dataSource.getCode())
                .orElseGet(() -> dataSourceRepository.save(DataSourceMapper.mapDataSource(dataSource)));
    }

}
