package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Source;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository.SourceRepository;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper.SourceMapper;

import org.springframework.stereotype.Service;

/**
 * The type Source service.
 */
@Service
public class SourceService {

    private final SourceRepository sourceRepository;

    /**
     * Instantiates a new Source service.
     *
     * @param sourceRepository the source repository
     */
    public SourceService(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    /**
     * Gets or create source.
     *
     * @param source the source
     * @return the or create source
     */
    public Source getOrCreateSource(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Source source) {
        return sourceRepository.findByCode(source.getCode())
                .orElseGet(() -> sourceRepository.save(SourceMapper.mapSource(source)));
    }

}
