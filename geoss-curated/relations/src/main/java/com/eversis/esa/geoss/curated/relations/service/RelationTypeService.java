package com.eversis.esa.geoss.curated.relations.service;

import com.eversis.esa.geoss.curated.relations.domain.RelationType;
import com.eversis.esa.geoss.curated.relations.model.RelationTypeModel;

/**
 * The interface Relation type service.
 */
public interface RelationTypeService {

    /**
     * Gets or create relation type.
     *
     * @param relationTypeModel the relation type model
     * @return the or create relation type
     */
    RelationType getOrCreateRelationType(RelationTypeModel relationTypeModel);

}
