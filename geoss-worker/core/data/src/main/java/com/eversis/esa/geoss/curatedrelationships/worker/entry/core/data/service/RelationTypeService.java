package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.RelationType;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository.RelationTypeRepository;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper.RelationTypeMapper;

import org.springframework.stereotype.Service;

/**
 * The type Relation type service.
 */
@Service
public class RelationTypeService {

    private final RelationTypeRepository relationTypeRepository;

    /**
     * Instantiates a new Relation type service.
     *
     * @param relationTypeRepository the relation type repository
     */
    public RelationTypeService(RelationTypeRepository relationTypeRepository) {
        this.relationTypeRepository = relationTypeRepository;
    }

    /**
     * Gets or create relation type.
     *
     * @param relationType the relation type
     * @return the or create relation type
     */
    public RelationType getOrCreateRelationType(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.RelationType relationType) {
        return relationTypeRepository.findByCode(relationType.getName())
                .orElseGet(() -> relationTypeRepository.save(RelationTypeMapper.mapRelationType(relationType)));
    }

}
