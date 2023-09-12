package com.eversis.esa.geoss.curated.common.mapper;

import com.eversis.esa.geoss.curated.common.domain.Type;
import com.eversis.esa.geoss.curated.common.model.TypeModel;

/**
 * The type Type mapper.
 */
public class TypeMapper {

    private TypeMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map type type.
     *
     * @param domainType the domain type
     * @return the type
     */
    public static Type mapType(TypeModel domainType) {
        Type dbType = new Type(domainType.getCode());
        dbType.setName(domainType.getName());
        return dbType;
    }

}
