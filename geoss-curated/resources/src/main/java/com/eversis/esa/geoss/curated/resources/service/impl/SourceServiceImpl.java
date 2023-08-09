package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.Source;
import com.eversis.esa.geoss.curated.resources.mapper.SourceMapper;
import com.eversis.esa.geoss.curated.resources.model.SourceModel;
import com.eversis.esa.geoss.curated.resources.repository.SourceRepository;
import com.eversis.esa.geoss.curated.resources.service.SourceService;

import org.springframework.stereotype.Service;

/**
 * The type Source service.
 */
@Service
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;

    /**
     * Instantiates a new Source service.
     *
     * @param sourceRepository the source repository
     */
    public SourceServiceImpl(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    /**
     * Gets or create source.
     *
     * @param source the source
     * @return the or create source
     */
    @Override
    public Source getOrCreateSource(SourceModel source) {
        return sourceRepository.findByCode(source.getCode())
                .orElseGet(() -> sourceRepository.save(SourceMapper.mapSource(source)));
    }

}
