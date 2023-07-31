package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.DashboardContents;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.DashboardModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.impl.DashboardModuleImpl;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.model.Dashboard;

public class DashboardFactory {

    private DashboardFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static DashboardModule createDashboardModule(DashboardContents dashboardContents) {
        Dashboard dashboard = new Dashboard();
        dashboard.setId(dashboardContents.getId());
        dashboard.setContent(dashboardContents.getContent());

        DashboardModule dashboardModule = new DashboardModuleImpl();
        dashboardModule.setDashboard(dashboard);
        return dashboardModule;
    }

}
