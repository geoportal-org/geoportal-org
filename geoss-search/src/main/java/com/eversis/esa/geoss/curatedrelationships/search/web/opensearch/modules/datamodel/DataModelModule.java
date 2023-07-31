package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.datamodel;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Facet;

import com.rometools.rome.feed.module.Module;

import java.util.List;
import java.util.Map;

public interface DataModelModule extends Module {

    Map<String, List<Facet>> getFacets();

    void setFacets(Map<String, List<Facet>> facets);
}
