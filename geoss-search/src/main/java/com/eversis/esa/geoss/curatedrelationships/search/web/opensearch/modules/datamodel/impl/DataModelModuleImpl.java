package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.datamodel.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Facet;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.datamodel.DataModelModule;

import com.rometools.rome.feed.CopyFrom;
import com.rometools.rome.feed.module.ModuleImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataModelModuleImpl extends ModuleImpl implements DataModelModule {

    private transient Map<String, List<Facet>> facets = new HashMap<>();

    public DataModelModuleImpl() {
        super(DataModelModule.class, DataModelModuleConstants.URI);
    }

    @Override
    public Map<String, List<Facet>> getFacets() {
        return facets;
    }

    @Override
    public void setFacets(Map<String, List<Facet>> facets) {
        this.facets = facets;
    }

    @Override
    public Class<? extends CopyFrom> getInterface() {
        return DataModelModule.class;
    }

    @Override
    public void copyFrom(CopyFrom obj) {
        final DataModelModule contributorModule = (DataModelModule) obj;
        setFacets(contributorModule.getFacets());
    }
}
