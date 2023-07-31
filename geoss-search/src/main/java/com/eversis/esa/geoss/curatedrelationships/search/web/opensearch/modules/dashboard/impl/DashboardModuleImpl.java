package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.impl;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.DashboardModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.model.Dashboard;

import com.rometools.rome.feed.CopyFrom;
import com.rometools.rome.feed.module.ModuleImpl;

public class DashboardModuleImpl extends ModuleImpl implements DashboardModule {

    private Dashboard dashboard;

    public DashboardModuleImpl() {
        super(DashboardModule.class, DashboardModuleConstants.MODULE_IDENTIFIER);
    }

    @Override
    public Dashboard getDashboard() {
        return dashboard;
    }

    @Override
    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    @Override
    public Class<DashboardModule> getInterface() {
        return DashboardModule.class;
    }

    @Override
    public void copyFrom(CopyFrom obj) {
        final DashboardModule dashboardModule = (DashboardModule) obj;
        setDashboard(dashboardModule.getDashboard());
    }
}
