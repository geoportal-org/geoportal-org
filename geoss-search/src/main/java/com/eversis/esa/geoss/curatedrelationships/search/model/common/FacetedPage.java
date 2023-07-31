package com.eversis.esa.geoss.curatedrelationships.search.model.common;

import java.util.List;
import java.util.Map;

public interface FacetedPage<T> extends Page<T> {

    Map<String, List<Facet>> getFacets();

}
