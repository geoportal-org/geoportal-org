package com.eversis.esa.geoss.curated.relations.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import javax.ws.rs.NotFoundException;

import com.eversis.esa.geoss.curated.common.email.EmailSender;
import com.eversis.esa.geoss.curated.relations.domain.UserRelation;
import com.eversis.esa.geoss.curated.relations.elasticsearch.service.ElasticsearchRelationService;
import com.eversis.esa.geoss.curated.relations.service.UserRelationService;
import com.eversis.esa.geoss.curated.relations.service.WorkflowRelationService;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * The type Workflow service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
@Validated
public class WorkflowRelationServiceImpl implements WorkflowRelationService {

    private static final String REALM_NAME = "geoss";

    private static final String ADMIN_GROUP_NAME = "administrator";

    private final UserRelationService userRelationService;

    private final ElasticsearchRelationService elasticsearchService;

    private final EmailSender emailSender;

    private final Keycloak keycloak;

    /**
     * Instantiates a new Workflow service.
     *
     * @param userRelationService the user relation service
     * @param elasticsearchService the elasticsearch service
     * @param emailSender the email sender
     * @param keycloak the keycloak
     */
    public WorkflowRelationServiceImpl(UserRelationService userRelationService,
            ElasticsearchRelationService elasticsearchService,
            EmailSender emailSender, @Qualifier("keycloakRelationClient") Keycloak keycloak) {
        this.userRelationService = userRelationService;
        this.elasticsearchService = elasticsearchService;
        this.emailSender = emailSender;
        this.keycloak = keycloak;
    }

    @Transactional
    @Override
    public void approveUserRelation(long userRelationId, String host) {
        log.info("Workflow approving user relation with id {}", userRelationId);
        UserRelation userRelation = userRelationService.approveUserResource(userRelationId);
        elasticsearchService.indexEntryRelation(userRelation.getEntryRelation());
        emailSender.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userRelationService.findUserRelation(userRelationId).getUserId())
                        .toRepresentation().getEmail(),
                Locale.getDefault(),
                "relation.approved.title",
                "emails/workflow-relation-approved.html",
                Map.of(
                        "id", userRelationId,
                        "name", userRelationService.findUserRelation(userRelationId).getEntryName(),
                        "relationUrl", host + "/relations/" + userRelationId
                ));
        log.info("Workflow approved user relation.");
    }

    @Transactional
    @Override
    public void denyUserRelation(long userRelationId, String host) {
        log.info("Workflow denying user relation with id {}", userRelationId);
        userRelationService.denyUserRelation(userRelationId);
        emailSender.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userRelationService.findUserRelation(userRelationId).getUserId())
                        .toRepresentation().getEmail(),
                Locale.getDefault(),
                "relation.denied.title",
                "emails/workflow-relation-denied.html",
                Map.of(
                        "id", userRelationId,
                        "name", userRelationService.findUserRelation(userRelationId).getEntryName(),
                        "relationUrl", host + "/relations/" + userRelationId
                ));
        log.info("Workflow denied user relation.");
    }

    @Transactional
    @Override
    public void pendingUserRelation(long userRelationId, String host) {
        log.info("Workflow pending user relation with id {}", userRelationId);
        userRelationService.pendingUserRelation(userRelationId);
        Optional<GroupRepresentation> groupByName = keycloak.realm(REALM_NAME).groups().groups().stream()
                .filter(groupRepresentation -> groupRepresentation.getName().equals(ADMIN_GROUP_NAME)).findAny();
        GroupRepresentation group =
                groupByName.orElseThrow(() -> new NotFoundException("Group not found with name " + ADMIN_GROUP_NAME));
        List<UserRepresentation> users = keycloak.realm(REALM_NAME).groups().group(group.getId()).members();
        users.forEach(userRepresentation ->
                emailSender.send(
                        userRepresentation.getEmail(),
                        Locale.getDefault(),
                        "relation.pending.title",
                        "emails/workflow-relation-pending.html",
                        Map.of(
                                "id", userRelationId,
                                "name", userRelationService.findUserRelation(userRelationId).getEntryName(),
                                "relationsVerifyUrl", host + "/relations/verify/" + userRelationId
                        )));
        log.info("Workflow pending user relation.");
    }

    @Transactional
    @Override
    public void deleteUserRelation(long userRelationId, String host) {
        log.info("Workflow delete user relation with id {}", userRelationId);
        UserRelation userRelation = userRelationService.findUserRelation(userRelationId);
        elasticsearchService.removeEntryRelationFromIndex(userRelation.getEntryRelation());
        Map<String, Object> variables = Map.of(
                "id", userRelationId,
                "name", userRelationService.findUserRelation(userRelationId).getEntryName()
        );
        String userId = userRelationService.findUserRelation(userRelationId).getUserId();
        userRelationService.deleteUserRelation(userRelationId);
        emailSender.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userId)
                        .toRepresentation().getEmail(), Locale.getDefault(),
                "relation.deleted.title", "emails/workflow-relation-deleted.html", variables);
        log.info("Workflow delete user relation.");
    }
}
