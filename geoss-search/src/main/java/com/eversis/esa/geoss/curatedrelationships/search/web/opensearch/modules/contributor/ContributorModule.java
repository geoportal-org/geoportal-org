package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.model.Contributor;

import com.rometools.rome.feed.module.Module;

/**
 * The interface Contributor module.
 */
public interface ContributorModule extends Module {

    /**
     * Gets contributor.
     *
     * @return the contributor
     */
    Contributor getContributor();

    /**
     * Sets contributor.
     *
     * @param contributor the contributor
     */
    void setContributor(final Contributor contributor);

}
