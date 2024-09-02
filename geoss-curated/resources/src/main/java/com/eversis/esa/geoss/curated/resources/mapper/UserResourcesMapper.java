package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.common.domain.Status;
import com.eversis.esa.geoss.curated.resources.domain.UserResource;
import com.eversis.esa.geoss.curated.resources.dto.UserResourceDTO;
import com.eversis.esa.geoss.curated.resources.model.UserResourceModel;
import com.eversis.esa.geoss.curated.resources.repository.UserResourceRepository;
import com.eversis.esa.geoss.curated.resources.service.EntryService;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type User resources mapper.
 */
@Component
public class UserResourcesMapper {

    private final EntryService entryService;

    private final UserResourceRepository userResourceRepository;

    /**
     * Instantiates a new User resources mapper.
     *
     * @param entryService the entry service
     */
    public UserResourcesMapper(EntryService entryService, UserResourceRepository userResourceRepository) {
        this.entryService = entryService;
        this.userResourceRepository = userResourceRepository;
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

    /**
     * Convert to dto user resource dto.
     *
     * @param userResource the user resource
     * @return the user resource dto
     */
    public UserResourceDTO convertToDto(UserResource userResource) {
        if (userResource == null) {
            return null;
        }
        UserResourceDTO dto = new UserResourceDTO();
        dto.setId(userResource.getId());
        dto.setUserId(userResource.getUserId());
        dto.setEntryName(userResource.getEntryName());
        dto.setTaskType(userResource.getTaskType());
        dto.setEntry(userResource.getEntry());
        dto.setStatus(userResource.getStatus());
        dto.setCreateDate(userResource.getCreateDate());
        dto.setModifiedDate(userResource.getModifiedDate());
        boolean hasOtherEntriesWithSameEntry = checkIfOtherEntriesExist(userResource);
        dto.setHasOtherResourcesWithSameEntry(hasOtherEntriesWithSameEntry);
        return dto;
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
        userResource.setEntry(entryService.saveEntry(model.getEntry()));
        return userResource;
    }

    private boolean checkIfOtherEntriesExist(UserResource userResource) {
        List<UserResource> userResources = userResourceRepository.findByEntryName(userResource.getEntryName());
        return userResources.size() > 1;
    }

}
