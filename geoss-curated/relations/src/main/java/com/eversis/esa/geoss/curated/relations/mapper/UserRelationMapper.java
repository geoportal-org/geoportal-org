package com.eversis.esa.geoss.curated.relations.mapper;

import com.eversis.esa.geoss.curated.relations.domain.Status;
import com.eversis.esa.geoss.curated.relations.domain.UserRelation;
import com.eversis.esa.geoss.curated.relations.model.UserRelationModel;
import com.eversis.esa.geoss.curated.relations.service.EntryRelationService;
import org.springframework.stereotype.Component;

/**
 * The type User relation mapper.
 */
@Component
public class UserRelationMapper {

    private final EntryRelationService entryRelationService;

    /**
     * Instantiates a new User relation mapper.
     *
     * @param entryRelationService the entry relation service
     */
    public UserRelationMapper(EntryRelationService entryRelationService) {
        this.entryRelationService = entryRelationService;
    }

    /**
     * Map to user relation user relation.
     *
     * @param userRelationDto the user relation dto
     * @return the user relation
     */
    public UserRelation mapToUserRelation(UserRelationModel userRelationDto) {
        return getUserRelation(userRelationDto);
    }

    /**
     * Map to user relation.
     *
     * @param userRelationDto the user relation dto
     * @param userRelation the user relation
     * @return the user relation
     */
    public UserRelation mapToUserRelation(UserRelationModel userRelationDto, UserRelation userRelation) {
        return getUserRelation(userRelationDto, userRelation);
    }

    private UserRelation getUserRelation(UserRelationModel model) {
        if (model == null) {
            return null;
        }
        UserRelation userRelation = new UserRelation();
        userRelation.setUserId(model.getUserId());
        userRelation.setEntryName(model.getEntryName());
        userRelation.setTaskType(model.getTaskType());
        userRelation.setEntryRelation(entryRelationService.getOrCreateEntry(model.getEntryRelation()));
        userRelation.setStatus(Status.DRAFT);
        return userRelation;
    }

    private UserRelation getUserRelation(UserRelationModel model, UserRelation userRelation) {
        if (model == null) {
            return null;
        }
        userRelation.setUserId(model.getUserId());
        userRelation.setEntryName(model.getEntryName());
        userRelation.setTaskType(model.getTaskType());
        userRelation.setEntryRelation(entryRelationService.getOrCreateEntry(model.getEntryRelation()));
        return userRelation;
    }

}
