package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The type Ckan facet.
 */
@Data
public class CkanFacet {

    protected long count;
    protected String name;
    @JsonProperty("display_name")
    protected String displayName;

}
