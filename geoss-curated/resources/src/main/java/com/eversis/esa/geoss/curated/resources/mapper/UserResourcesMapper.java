package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.common.domain.Status;
import com.eversis.esa.geoss.curated.resources.domain.UserResource;
import com.eversis.esa.geoss.curated.resources.model.UserResourceModel;
import com.eversis.esa.geoss.curated.resources.service.EntryService;

import org.springframework.stereotype.Component;

/**
 * The type User resources mapper.
 */
@Component
public class UserResourcesMapper {

    private final EntryService entryService;

    /**
     * Instantiates a new User resources mapper.
     *
     * @param entryService the entry service
     */
    public UserResourcesMapper(EntryService entryService) {
        this.entryService = entryService;
    }

    /**
     * Map to user resource user resource.
     *
     * @param model the model
     * @return the user resource
     */
    public UserResource mapToUserResource(UserResourceModel model) {
        return getUserResource(model);
    }

    /**
     * Map to user resource user resource.
     *
     * @param model the model
     * @param userResource the user resource
     * @return the user resource
     */
    public UserResource mapToUserResource(UserResourceModel model, UserResource userResource) {
        return getUserResource(model, userResource);
    }

    private UserResource getUserResource(UserResourceModel model) {
        if (model == null) {
            return null;
        }
        UserResource userResource = new UserResource();
        userResource.setUserId(model.getUserId());
        userResource.setEntryName(model.getEntryName());
        userResource.setTaskType(model.getTaskType());
        userResource.setEntry(entryService.getOrCreateEntry(model.getEntry()));
        userResource.setStatus(Status.DRAFT);
        return userResource;
    }

    private UserResource getUserResource(UserResourceModel model, UserResource userResource) {
        if (model == null) {
            return null;
        }
        userResource.setUserId(model.getUserId());
        userResource.setEntryName(model.getEntryName());
        userResource.setTaskType(model.getTaskType());
        userResource.setEntry(entryService.getOrCreateEntry(model.getEntry()));
        return userResource;
    }

}
