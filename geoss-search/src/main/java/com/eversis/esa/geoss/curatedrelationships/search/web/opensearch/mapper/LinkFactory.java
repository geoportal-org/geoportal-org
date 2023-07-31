package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.logo.AtomLogoModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.logo.impl.AtomLogoModuleImpl;

import com.rometools.rome.feed.atom.Link;

/**
 * The type Link factory.
 */
public class LinkFactory {

    private LinkFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create atom logo link atom logo module.
     *
     * @param logoURL the logo url
     * @return the atom logo module
     */
    public static AtomLogoModule createAtomLogoLink(String logoURL) {
        AtomLogoModule atomLinkModule = new AtomLogoModuleImpl();
        Link link = new Link();
        link.setHref(logoURL);
        atomLinkModule.setLink(link);
        return atomLinkModule;
    }

}
