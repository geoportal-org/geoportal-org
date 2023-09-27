package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Type;

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
    public static Type mapType(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Type domainType) {
        Type dbType = new Type(domainType.getCode());
        dbType.setName(domainType.getName());
        return dbType;
    }

}
