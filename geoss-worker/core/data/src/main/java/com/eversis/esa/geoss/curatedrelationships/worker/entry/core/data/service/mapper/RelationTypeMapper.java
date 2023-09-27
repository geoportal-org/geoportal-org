package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.RelationType;

/**
 * The type Relation type mapper.
 */
public class RelationTypeMapper {

    private RelationTypeMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map relation type relation type.
     *
     * @param domainRelationType the domain relation type
     * @return the relation type
     */
    public static RelationType mapRelationType(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.RelationType domainRelationType) {
        RelationType dbRelationType = new RelationType(domainRelationType.getCode());
        dbRelationType.setName(domainRelationType.getName());
        return dbRelationType;
    }

}
