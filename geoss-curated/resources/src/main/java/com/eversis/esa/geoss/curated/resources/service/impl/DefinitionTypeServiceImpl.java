package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.DefinitionType;
import com.eversis.esa.geoss.curated.resources.mapper.DefinitionTypeMapper;
import com.eversis.esa.geoss.curated.resources.model.DefinitionTypeModel;
import com.eversis.esa.geoss.curated.resources.repository.DefinitionTypeRepository;
import com.eversis.esa.geoss.curated.resources.service.DefinitionTypeService;

import org.springframework.stereotype.Service;

/**
 * The type Definition type service.
 */
@Service
public class DefinitionTypeServiceImpl implements DefinitionTypeService {

    private final DefinitionTypeRepository definitionTypeRepository;

    /**
     * Instantiates a new Definition type service.
     *
     * @param definitionTypeRepository the definition type repository
     */
    public DefinitionTypeServiceImpl(DefinitionTypeRepository definitionTypeRepository) {
        this.definitionTypeRepository = definitionTypeRepository;
    }

    @Override
    public DefinitionType getOrCreateDefinitionType(DefinitionTypeModel definitionType) {
        return definitionTypeRepository.findByCode(definitionType.getCode())
                .orElseGet(() -> definitionTypeRepository.save(DefinitionTypeMapper.mapDefinitionType(definitionType)));
    }

}
