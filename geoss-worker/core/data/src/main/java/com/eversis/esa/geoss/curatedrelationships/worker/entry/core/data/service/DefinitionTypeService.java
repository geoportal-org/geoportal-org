package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.DefinitionType;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository.DefinitionTypeRepository;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper.DefinitionTypeMapper;

import org.springframework.stereotype.Service;

/**
 * The type Definition type service.
 */
@Service
public class DefinitionTypeService {

    private final DefinitionTypeRepository definitionTypeRepository;

    /**
     * Instantiates a new Definition type service.
     *
     * @param definitionTypeRepository the definition type repository
     */
    public DefinitionTypeService(DefinitionTypeRepository definitionTypeRepository) {
        this.definitionTypeRepository = definitionTypeRepository;
    }

    /**
     * Gets or create definition type.
     *
     * @param definitionType the definition type
     * @return the or create definition type
     */
    public DefinitionType getOrCreateDefinitionType(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DefinitionType definitionType) {
        return definitionTypeRepository.findByCode(definitionType.getCode())
                .orElseGet(() -> definitionTypeRepository.save(DefinitionTypeMapper.mapDefinitionType(definitionType)));
    }

}
