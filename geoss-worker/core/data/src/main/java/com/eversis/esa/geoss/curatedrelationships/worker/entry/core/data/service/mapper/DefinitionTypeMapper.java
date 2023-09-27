package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.DefinitionType;

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
    public static DefinitionType mapDefinitionType(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DefinitionType
                    domainDefinitionType) {
        DefinitionType dbDefinitionType = new DefinitionType(domainDefinitionType.getCode());
        dbDefinitionType.setName(domainDefinitionType.getName());
        return dbDefinitionType;
    }

}
