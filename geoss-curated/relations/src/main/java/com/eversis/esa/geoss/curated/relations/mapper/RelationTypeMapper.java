package com.eversis.esa.geoss.curated.relations.mapper;

import com.eversis.esa.geoss.curated.relations.domain.RelationType;
import com.eversis.esa.geoss.curated.relations.model.RelationTypeModel;

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
     * @param relationTypeModel the relation type model
     * @return the relation type
     */
    public static RelationType mapRelationType(RelationTypeModel relationTypeModel) {
        RelationType dbRelationType = new RelationType(relationTypeModel.getCode());
        dbRelationType.setName(relationTypeModel.getName());
        return dbRelationType;
    }

}




