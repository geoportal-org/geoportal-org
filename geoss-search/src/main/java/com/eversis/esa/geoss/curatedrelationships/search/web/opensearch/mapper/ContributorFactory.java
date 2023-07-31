package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Organisation;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.ContributorModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.impl.ContributorModuleImpl;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.model.Contributor;

/**
 * The type Contributor factory.
 */
public class ContributorFactory {

    private ContributorFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create contributor module contributor module.
     *
     * @param organisation the organisation
     * @return the contributor module
     */
    public static ContributorModule createContributorModule(Organisation organisation) {
        Contributor contributor = new Contributor();
        contributor.setOrgName(organisation.getTitle());
        contributor.setEmail(organisation.getEmail());

        ContributorModule contributorModule = new ContributorModuleImpl();
        contributorModule.setContributor(contributor);
        return contributorModule;
    }

}
