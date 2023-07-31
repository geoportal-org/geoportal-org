package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.model.Contributor;

import com.rometools.rome.feed.module.Module;

public interface ContributorModule extends Module {

    Contributor getContributor();

    void setContributor(final Contributor contributor);

}
