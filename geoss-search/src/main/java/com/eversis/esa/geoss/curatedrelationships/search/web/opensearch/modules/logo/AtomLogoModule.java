package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.logo;

import com.rometools.rome.feed.atom.Link;
import com.rometools.rome.feed.module.Module;

/**
 * The interface Atom logo module.
 */
public interface AtomLogoModule extends Module {

    /**
     * Gets link.
     *
     * @return the link
     */
    Link getLink();

    /**
     * Sets link.
     *
     * @param link the link
     */
    void setLink(Link link);

}
