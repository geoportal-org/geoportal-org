package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.DashboardContents;
import com.eversis.esa.geoss.curated.resources.mapper.DashboardContentsMapper;
import com.eversis.esa.geoss.curated.resources.model.DashboardContentsModel;
import com.eversis.esa.geoss.curated.resources.repository.DashboardContentsRepository;
import com.eversis.esa.geoss.curated.resources.service.DashboardContentsService;
import org.springframework.stereotype.Service;

/**
 * The type Dashboard contents service.
 */
@Service
public class DashboardContentsServiceImpl implements DashboardContentsService {

    private final DashboardContentsRepository dashboardContentsRepository;

    /**
     * Instantiates a new Dashboard contents service.
     *
     * @param dashboardContentsRepository the dashboard contents repository
     */
    public DashboardContentsServiceImpl(DashboardContentsRepository dashboardContentsRepository) {
        this.dashboardContentsRepository = dashboardContentsRepository;
    }

    @Override
    public DashboardContents getOrCreateDashboardContents(DashboardContentsModel dashboardContents) {
        if ((dashboardContents.getContent() != null) && (!dashboardContents.getContent().isEmpty())){
            return dashboardContentsRepository.findByContent(dashboardContents.getContent())
                    .orElseGet(() -> dashboardContentsRepository.save(DashboardContentsMapper.mapDashboardContents(dashboardContents)));
        } else {
            return null;
        }
    }

}
