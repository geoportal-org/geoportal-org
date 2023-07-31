package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.impl;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.ContributorModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.model.Contributor;

import com.rometools.rome.feed.CopyFrom;
import com.rometools.rome.feed.module.ModuleImpl;

public class ContributorModuleImpl extends ModuleImpl implements ContributorModule {

    private Contributor contributor;

    public ContributorModuleImpl() {
        super(ContributorModule.class, ContributorModuleConstants.MODULE_IDENTIFIER);
    }

    @Override
    public Contributor getContributor() {
        return contributor;
    }

    @Override
    public void setContributor(Contributor contributor) {
        this.contributor = contributor;
    }

    @Override
    public Class<ContributorModule> getInterface() {
        return ContributorModule.class;
    }

    @Override
    public void copyFrom(CopyFrom obj) {
        final ContributorModule contributorModule = (ContributorModule) obj;
        setContributor(contributorModule.getContributor());
    }
}
