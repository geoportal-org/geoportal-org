package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.resources.domain.DefinitionType;
import com.eversis.esa.geoss.curated.resources.model.DefinitionTypeModel;

/**
 * The type Definition type mapper.
 */
public class DefinitionTypeMapper {

    private DefinitionTypeMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map definition type definition type.
     *
     * @param domainDefinitionType the domain definition type
     * @return the definition type
     */
    public static DefinitionType mapDefinitionType(DefinitionTypeModel domainDefinitionType) {
        DefinitionType dbDefinitionType = new DefinitionType(domainDefinitionType.getCode());
        dbDefinitionType.setName(domainDefinitionType.getName());
        return dbDefinitionType;
    }

}
