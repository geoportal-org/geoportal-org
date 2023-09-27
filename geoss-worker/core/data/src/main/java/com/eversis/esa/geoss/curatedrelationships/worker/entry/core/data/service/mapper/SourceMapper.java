package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Source;

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
    public static Source mapSource(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Source domainSource) {
        Source dbSource = new Source(domainSource.getCode());
        dbSource.setTerm(domainSource.getTerm());
        return dbSource;
    }

}
