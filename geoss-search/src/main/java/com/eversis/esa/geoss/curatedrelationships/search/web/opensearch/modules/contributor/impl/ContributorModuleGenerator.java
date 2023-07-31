package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.impl;

import com.eversis.esa.geoss.curatedrelationships.search.utils.StringUtils;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper.ElementFactory;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.ContributorModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.model.Contributor;

import com.rometools.rome.feed.module.Module;
import com.rometools.rome.io.ModuleGenerator;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.Collections;
import java.util.Set;

public class ContributorModuleGenerator implements ModuleGenerator {

    @Override
    public String getNamespaceUri() {
        return ContributorModuleConstants.MODULE_IDENTIFIER;
    }

    @Override
    public Set<Namespace> getNamespaces() {
        return Collections.emptySet();
    }

    @Override
    public void generate(final Module module, final Element element) {
        Element root = element;
        while (root.getParent() instanceof Element) {
            root = (Element) element.getParent();
        }

        ContributorModule contributorModule = (ContributorModule) module;
        Contributor contributor = contributorModule.getContributor();
        if (contributor != null) {
            Element contributorElement = new Element("contributor");

            if (StringUtils.isNotBlank(contributor.getOrgName())) {
                contributorElement.addContent(ElementFactory.createElement("orgName", contributor.getOrgName()));
            }
            if (StringUtils.isNotBlank(contributor.getIndName())) {
                contributorElement.addContent(ElementFactory.createElement("indName", contributor.getIndName()));
            }
            if (StringUtils.isNotBlank(contributor.getEmail())) {
                contributorElement.addContent(ElementFactory.createElement("email", contributor.getEmail()));
            }
            root.addContent(contributorElement);
        }

    }

}
