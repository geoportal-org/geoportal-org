package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.mapper.impl;

import java.io.IOException;

public interface ZenodoResultMapper<T, S> {

    T mapToObject(S result) throws IOException;

}
