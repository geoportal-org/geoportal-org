package com.eversis.esa.geoss.curatedrelationships.search.model.common.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Facet;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacetedPageImpl<T> extends PageImpl<T> implements FacetedPage<T> {

    private Map<String, List<Facet>> facets = new HashMap<>();

    public FacetedPageImpl(List<T> content) {
        super(content);
    }

    public FacetedPageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public FacetedPageImpl(List<T> content, Pageable pageable, long total, Map<String, List<Facet>> facets) {
        super(content, pageable, total);
        this.facets = facets;
    }

    @Override
    public Map<String, List<Facet>> getFacets() {
        return facets;
    }

}
