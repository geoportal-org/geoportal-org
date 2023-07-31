package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.logo;

import com.rometools.rome.feed.atom.Link;
import com.rometools.rome.feed.module.Module;

public interface AtomLogoModule extends Module {

    Link getLink();

    void setLink(Link link);

}
