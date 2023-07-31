package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.base;

import lombok.Data;

@Data
public class CkanResponse<T> {

    protected boolean success;
    protected String help;

    protected CkanSearchResult<T> result;

}
