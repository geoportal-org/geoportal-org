package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.model.Dashboard;

import com.rometools.rome.feed.module.Module;

public interface DashboardModule extends Module {

    Dashboard getDashboard();

    void setDashboard(final Dashboard dashboard);

}
