package com.eversis.esa.geoss.curated.dashboards.mapper;

import com.eversis.esa.geoss.curated.common.domain.Status;
import com.eversis.esa.geoss.curated.dashboards.domain.UserDashboard;
import com.eversis.esa.geoss.curated.dashboards.model.UserDashboardModel;
import com.eversis.esa.geoss.curated.resources.service.EntryService;

import org.springframework.stereotype.Component;

/**
 * The type User dashboards mapper.
 */
@Component
public class UserDashboardMapper {

    private final EntryService entryService;

    /**
     * Instantiates a new User dashboards mapper.
     *
     * @param entryService the entry service
     */
    public UserDashboardMapper(EntryService entryService) {
        this.entryService = entryService;
    }

    /**
     * Map to user dashboard user dashboard.
     *
     * @param model the model
     * @return the user dashboard
     */
    public UserDashboard mapToUserDashboard(UserDashboardModel model) {
        return getUserDashboard(model);
    }

    /**
     * Map to user dashboard user dashboard.
     *
     * @param model the model
     * @param userDashboard the user dashboard
     * @return the user dashboard
     */
    public UserDashboard mapToUserDashboard(UserDashboardModel model, UserDashboard userDashboard) {
        return getUserDashboard(model, userDashboard);
    }

    private UserDashboard getUserDashboard(UserDashboardModel model) {
        if (model == null) {
            return null;
        }
        UserDashboard userDashboard = new UserDashboard();
        userDashboard.setUserId(model.getUserId());
        userDashboard.setEntryName(model.getEntryName());
        userDashboard.setTaskType(model.getTaskType());
        userDashboard.setEntry(entryService.getOrCreateEntry(model.getEntry()));
        userDashboard.setStatus(Status.DRAFT);
        return userDashboard;
    }

    private UserDashboard getUserDashboard(UserDashboardModel model, UserDashboard userDashboard) {
        if (model == null) {
            return null;
        }
        userDashboard.setUserId(model.getUserId());
        userDashboard.setEntryName(model.getEntryName());
        userDashboard.setTaskType(model.getTaskType());
        userDashboard.setEntry(entryService.getOrCreateEntry(model.getEntry()));
        return userDashboard;
    }

}
