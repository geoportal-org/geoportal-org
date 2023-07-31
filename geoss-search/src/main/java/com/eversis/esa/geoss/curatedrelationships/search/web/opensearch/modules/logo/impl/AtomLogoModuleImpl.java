package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.logo.impl;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.logo.AtomLogoModule;

import com.rometools.rome.feed.CopyFrom;
import com.rometools.rome.feed.atom.Link;
import com.rometools.rome.feed.module.ModuleImpl;

/**
 * The type Atom logo module.
 */
public class AtomLogoModuleImpl extends ModuleImpl implements AtomLogoModule {

    private Link link;

    /**
     * Instantiates a new Atom logo module.
     */
    public AtomLogoModuleImpl() {
        super(AtomLogoModule.class, AtomLogoModuleConstants.MODULE_IDENTIFIER);
    }

    @Override
    public Link getLink() {
        return this.link;
    }

    @Override
    public void setLink(Link link) {
        this.link = link;
    }

    /**
     * Gets uri.
     *
     * @return the uri
     */
    @Override
    public String getUri() {
        return AtomLogoModuleConstants.URI;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        AtomLogoModuleImpl cloned = new AtomLogoModuleImpl();
        cloned.setLink((Link) this.getLink().clone());
        return cloned;
    }

    /**
     * Gets interface.
     *
     * @return the interface
     */
    @Override
    public Class<? extends CopyFrom> getInterface() {
        return AtomLogoModule.class;
    }

    /**
     * Copy from.
     *
     * @param obj the obj
     */
    @Override
    public void copyFrom(CopyFrom obj) {
        AtomLogoModule logoModule = (AtomLogoModule) obj;
        this.setLink(logoModule.getLink());
    }
}
