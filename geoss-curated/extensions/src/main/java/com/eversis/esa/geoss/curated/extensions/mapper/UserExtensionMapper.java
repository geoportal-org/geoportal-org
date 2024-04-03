package com.eversis.esa.geoss.curated.extensions.mapper;

import java.util.List;

import com.eversis.esa.geoss.curated.common.domain.Status;
import com.eversis.esa.geoss.curated.extensions.domain.UserExtension;
import com.eversis.esa.geoss.curated.extensions.dto.UserExtensionDTO;
import com.eversis.esa.geoss.curated.extensions.model.UserExtensionModel;
import com.eversis.esa.geoss.curated.extensions.repository.UserExtensionRepository;
import com.eversis.esa.geoss.curated.extensions.service.EntryExtensionService;

import org.springframework.stereotype.Component;

/**
 * The type User extension mapper.
 */
@Component
public class UserExtensionMapper {

    private final EntryExtensionService entryExtensionService;

    private final UserExtensionRepository userExtensionRepository;

    /**
     * Instantiates a new User extension mapper.
     *
     * @param entryExtensionService the entry extension service
     * @param userExtensionRepository the user extension repository
     */
    public UserExtensionMapper(EntryExtensionService entryExtensionService,
            UserExtensionRepository userExtensionRepository) {
        this.entryExtensionService = entryExtensionService;
        this.userExtensionRepository = userExtensionRepository;
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

    /**
     * Convert to dto user extension dto.
     *
     * @param userExtension the user extension
     * @return the user extension dto
     */
    public UserExtensionDTO convertToDto(UserExtension userExtension) {
        if (userExtension == null) {
            return null;
        }
        UserExtensionDTO dto = new UserExtensionDTO();
        dto.setId(userExtension.getId());
        dto.setUserId(userExtension.getUserId());
        dto.setEntryName(userExtension.getEntryName());
        dto.setTaskType(userExtension.getTaskType());
        dto.setEntryExtension(userExtension.getEntryExtension());
        dto.setStatus(userExtension.getStatus());
        dto.setCreateDate(userExtension.getCreateDate());
        dto.setModifiedDate(userExtension.getModifiedDate());
        boolean hasOtherExtensionsWithSameEntry = checkIfOtherEntriesExist(userExtension);
        dto.setHasOtherExtensionsWithSameEntry(hasOtherExtensionsWithSameEntry);
        return dto;
    }

    private boolean checkIfOtherEntriesExist(UserExtension userExtension) {
        List<UserExtension> userExtensions = userExtensionRepository.findByEntryName(userExtension.getEntryName());
        return userExtensions.size() > 1;
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
        userExtension.setEntryExtension(entryExtensionService.saveEntryExtension(model.getEntryExtension()));
        return userExtension;
    }

}
