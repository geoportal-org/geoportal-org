package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.model.ZenodoResult;

public interface ZenodoMapper<T> {

    /**
     * Wraps single Zenodo result entry to custom object.
     *
     * @param result search result from Zenodo
     * @return custom object
     */
    T mapSearchResult(ZenodoResult result);

}
