package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.DashboardContents;
import com.eversis.esa.geoss.curated.resources.model.DashboardContentsModel;

/**
 * The interface Dashboard contents service.
 */
public interface DashboardContentsService {

    /**
     * Gets or create dashboard contents.
     *
     * @param dashboardContents the dashboard contents
     * @return the or create dashboard contents
     */
    DashboardContents getOrCreateDashboardContents(DashboardContentsModel dashboardContents);

}
