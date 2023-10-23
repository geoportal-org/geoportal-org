package com.eversis.esa.geoss.curated.relations.service.impl;

import com.eversis.esa.geoss.curated.relations.domain.RelationType;
import com.eversis.esa.geoss.curated.relations.mapper.RelationTypeMapper;
import com.eversis.esa.geoss.curated.relations.model.RelationTypeModel;
import com.eversis.esa.geoss.curated.relations.repository.RelationTypeRepository;
import com.eversis.esa.geoss.curated.relations.service.RelationTypeService;

import org.springframework.stereotype.Service;

/**
 * The type Relation type service.
 */
@Service
public class RelationTypeServiceImpl implements RelationTypeService {

    private final RelationTypeRepository relationTypeRepository;

    /**
     * Instantiates a new Relation type service.
     *
     * @param relationTypeRepository the relation type repository
     */
    public RelationTypeServiceImpl(RelationTypeRepository relationTypeRepository) {
        this.relationTypeRepository = relationTypeRepository;
    }

    @Override
    public RelationType getOrCreateRelationType(RelationTypeModel relationTypeModel) {
        return relationTypeRepository.findByCode(relationTypeModel.getCode())
                .orElseGet(() -> relationTypeRepository.save(RelationTypeMapper.mapRelationType(relationTypeModel)));
    }

}
