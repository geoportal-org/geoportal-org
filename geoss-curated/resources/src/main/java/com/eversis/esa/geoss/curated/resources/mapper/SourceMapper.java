package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.resources.domain.Source;
import com.eversis.esa.geoss.curated.resources.model.SourceModel;

/**
 * The type Source mapper.
 */
public class SourceMapper {

    private SourceMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map source source.
     *
     * @param domainSource the domain source
     * @return the source
     */
    public static Source mapSource(SourceModel domainSource) {
        Source dbSource = new Source(domainSource.getCode());
        dbSource.setTerm(domainSource.getTerm());
        dbSource.setIsCustom(1);
        return dbSource;
    }

}
