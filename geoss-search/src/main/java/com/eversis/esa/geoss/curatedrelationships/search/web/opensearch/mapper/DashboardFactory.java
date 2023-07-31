package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.DashboardContents;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.DashboardModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.impl.DashboardModuleImpl;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.model.Dashboard;

/**
 * The type Dashboard factory.
 */
public class DashboardFactory {

    private DashboardFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create dashboard module dashboard module.
     *
     * @param dashboardContents the dashboard contents
     * @return the dashboard module
     */
    public static DashboardModule createDashboardModule(DashboardContents dashboardContents) {
        Dashboard dashboard = new Dashboard();
        dashboard.setId(dashboardContents.getId());
        dashboard.setContent(dashboardContents.getContent());

        DashboardModule dashboardModule = new DashboardModuleImpl();
        dashboardModule.setDashboard(dashboard);
        return dashboardModule;
    }

}
