package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper;

import java.io.IOException;

public interface CkanResultMapper<T, S> {

    T mapToObject(S result) throws IOException;

}
