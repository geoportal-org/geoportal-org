package com.eversis.esa.geoss.curated.relations.mapper;

import com.eversis.esa.geoss.curated.relations.domain.Types;
import com.eversis.esa.geoss.curated.relations.model.TypeModel;

/**
 * The type Types mapper.
 */
public class TypesMapper {

    private TypesMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map type types.
     *
     * @param typeModel the type model
     * @return the types
     */
    public static Types mapType(TypeModel typeModel) {
        Types dbType = new Types(typeModel.getCode());
        dbType.setName(typeModel.getName());
        return dbType;
    }

}
