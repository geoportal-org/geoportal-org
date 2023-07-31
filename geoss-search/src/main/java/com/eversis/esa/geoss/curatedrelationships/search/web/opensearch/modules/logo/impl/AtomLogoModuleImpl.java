package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.logo.impl;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.logo.AtomLogoModule;

import com.rometools.rome.feed.CopyFrom;
import com.rometools.rome.feed.atom.Link;
import com.rometools.rome.feed.module.ModuleImpl;

public class AtomLogoModuleImpl extends ModuleImpl implements AtomLogoModule {

    private Link link;

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

    @Override
    public Class<? extends CopyFrom> getInterface() {
        return AtomLogoModule.class;
    }

    @Override
    public void copyFrom(CopyFrom obj) {
        AtomLogoModule logoModule = (AtomLogoModule) obj;
        this.setLink(logoModule.getLink());
    }
}
