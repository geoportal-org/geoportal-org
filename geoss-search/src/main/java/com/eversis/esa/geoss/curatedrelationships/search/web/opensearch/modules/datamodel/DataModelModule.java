package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.datamodel;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Facet;

import com.rometools.rome.feed.module.Module;

import java.util.List;
import java.util.Map;

/**
 * The interface Data model module.
 */
public interface DataModelModule extends Module {

    /**
     * Gets facets.
     *
     * @return the facets
     */
    Map<String, List<Facet>> getFacets();

    /**
     * Sets facets.
     *
     * @param facets the facets
     */
    void setFacets(Map<String, List<Facet>> facets);
}
