package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.base;

import lombok.Data;

import java.util.List;

/**
 * The type Ckan facet wrapper.
 */
@Data
public class CkanFacetWrapper {

    protected List<CkanFacet> items;
    protected String title;

}
