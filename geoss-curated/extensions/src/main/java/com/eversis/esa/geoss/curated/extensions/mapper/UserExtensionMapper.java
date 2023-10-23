package com.eversis.esa.geoss.curated.extensions.mapper;

import com.eversis.esa.geoss.curated.common.domain.Status;
import com.eversis.esa.geoss.curated.extensions.domain.UserExtension;
import com.eversis.esa.geoss.curated.extensions.model.UserExtensionModel;
import com.eversis.esa.geoss.curated.extensions.service.EntryExtensionService;

import org.springframework.stereotype.Component;

/**
 * The type User extension mapper.
 */
@Component
public class UserExtensionMapper {

    private final EntryExtensionService entryExtensionService;

    /**
     * Instantiates a new User extension mapper.
     *
     * @param entryExtensionService the entry extension service
     */
    public UserExtensionMapper(EntryExtensionService entryExtensionService) {
        this.entryExtensionService = entryExtensionService;
    }

    /**
     * Map to user user extension user extension.
     *
     * @param model the model
     * @return the user extension
     */
    public UserExtension mapToUserUserExtension(UserExtensionModel model) {
        return getUserExtension(model);
    }

    /**
     * Map to user user extension user extension.
     *
     * @param model the model
     * @param userExtension the user extension
     * @return the user extension
     */
    public UserExtension mapToUserUserExtension(UserExtensionModel model, UserExtension userExtension) {
        return getUserExtension(model, userExtension);
    }

    private UserExtension getUserExtension(UserExtensionModel model) {
        if (model == null) {
            return null;
        }
        UserExtension userExtension = new UserExtension();
        userExtension.setUserId(model.getUserId());
        userExtension.setEntryName(model.getEntryName());
        userExtension.setDescription(model.getDescription());
        userExtension.setTaskType(model.getTaskType());
        userExtension.setEntryExtension(entryExtensionService.getOrCreateEntryExtension(model.getEntryExtension()));
        userExtension.setStatus(Status.DRAFT);
        return userExtension;
    }

    private UserExtension getUserExtension(UserExtensionModel model, UserExtension userExtension) {
        if (model == null) {
            return null;
        }
        userExtension.setUserId(model.getUserId());
        userExtension.setEntryName(model.getEntryName());
        userExtension.setDescription(model.getDescription());
        userExtension.setTaskType(model.getTaskType());
        userExtension.setEntryExtension(entryExtensionService.getOrCreateEntryExtension(model.getEntryExtension()));
        return userExtension;
    }

}
