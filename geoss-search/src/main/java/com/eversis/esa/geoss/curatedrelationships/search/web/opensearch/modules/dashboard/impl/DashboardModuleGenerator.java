package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.impl;

import com.eversis.esa.geoss.curatedrelationships.search.utils.StringUtils;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper.ElementFactory;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.DashboardModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.model.Dashboard;

import com.rometools.rome.feed.module.Module;
import com.rometools.rome.io.ModuleGenerator;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.Collections;
import java.util.Set;

public class DashboardModuleGenerator implements ModuleGenerator {

    @Override
    public String getNamespaceUri() {
        return DashboardModuleConstants.MODULE_IDENTIFIER;
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

        DashboardModule dashboardModule = (DashboardModule) module;
        Dashboard dashboard = dashboardModule.getDashboard();
        if (dashboard != null) {
            Element dashboardElement = new Element("dashboard");

            if (StringUtils.isNotBlank(String.valueOf(dashboard.getId()))) {
                dashboardElement.addContent(ElementFactory.createElement("id", String.valueOf(dashboard.getId())));
            }
            if (StringUtils.isNotBlank(dashboard.getContent())) {
                dashboardElement.addContent(ElementFactory.createElement("content", dashboard.getContent()));
            }
            root.addContent(dashboardElement);
        }

    }

}
