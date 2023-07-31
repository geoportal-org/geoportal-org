package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.impl;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.DashboardModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.model.Dashboard;

import com.rometools.rome.feed.CopyFrom;
import com.rometools.rome.feed.module.ModuleImpl;

/**
 * The type Dashboard module.
 */
public class DashboardModuleImpl extends ModuleImpl implements DashboardModule {

    private Dashboard dashboard;

    /**
     * Instantiates a new Dashboard module.
     */
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

    /**
     * Gets interface.
     *
     * @return the interface
     */
    @Override
    public Class<DashboardModule> getInterface() {
        return DashboardModule.class;
    }

    /**
     * Copy from.
     *
     * @param obj the obj
     */
    @Override
    public void copyFrom(CopyFrom obj) {
        final DashboardModule dashboardModule = (DashboardModule) obj;
        setDashboard(dashboardModule.getDashboard());
    }
}
