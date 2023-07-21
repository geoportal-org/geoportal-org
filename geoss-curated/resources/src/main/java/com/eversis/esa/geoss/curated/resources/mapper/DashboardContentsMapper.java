package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.resources.domain.DashboardContents;
import com.eversis.esa.geoss.curated.resources.model.DashboardContentsModel;

/**
 * The type Dashboard contents mapper.
 */
public class DashboardContentsMapper {

    private DashboardContentsMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map dashboard contents dashboard contents.
     *
     * @param dashboardContents the dashboard contents
     * @return the dashboard contents
     */
    public static DashboardContents mapDashboardContents(DashboardContentsModel dashboardContents) {
        DashboardContents dbDashboardContents = new DashboardContents(dashboardContents.getContent());
        dbDashboardContents.setContent(dashboardContents.getContent());
        return dbDashboardContents;
    }

}
