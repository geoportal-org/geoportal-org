package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.model.Dashboard;

import com.rometools.rome.feed.module.Module;

/**
 * The interface Dashboard module.
 */
public interface DashboardModule extends Module {

    /**
     * Gets dashboard.
     *
     * @return the dashboard
     */
    Dashboard getDashboard();

    /**
     * Sets dashboard.
     *
     * @param dashboard the dashboard
     */
    void setDashboard(final Dashboard dashboard);

}
