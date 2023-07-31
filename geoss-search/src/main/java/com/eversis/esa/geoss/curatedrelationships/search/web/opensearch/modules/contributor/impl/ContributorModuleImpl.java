package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.impl;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.ContributorModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.model.Contributor;

import com.rometools.rome.feed.CopyFrom;
import com.rometools.rome.feed.module.ModuleImpl;

/**
 * The type Contributor module.
 */
public class ContributorModuleImpl extends ModuleImpl implements ContributorModule {

    private Contributor contributor;

    /**
     * Instantiates a new Contributor module.
     */
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

    /**
     * Gets interface.
     *
     * @return the interface
     */
    @Override
    public Class<ContributorModule> getInterface() {
        return ContributorModule.class;
    }

    /**
     * Copy from.
     *
     * @param obj the obj
     */
    @Override
    public void copyFrom(CopyFrom obj) {
        final ContributorModule contributorModule = (ContributorModule) obj;
        setContributor(contributorModule.getContributor());
    }
}
