package com.eversis.esa.geoss.curated.workflow.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import com.eversis.esa.geoss.curated.common.email.EmailEventPublisher;
import com.eversis.esa.geoss.curated.dashboards.domain.UserDashboard;
import com.eversis.esa.geoss.curated.dashboards.service.UserDashboardService;
import com.eversis.esa.geoss.curated.elasticsearch.service.ElasticsearchService;
import com.eversis.esa.geoss.curated.extensions.domain.UserExtension;
import com.eversis.esa.geoss.curated.extensions.service.UserExtensionService;
import com.eversis.esa.geoss.curated.relations.domain.UserRelation;
import com.eversis.esa.geoss.curated.relations.service.UserRelationService;
import com.eversis.esa.geoss.curated.resources.domain.UserResource;
import com.eversis.esa.geoss.curated.resources.service.UserResourceService;

import com.eversis.esa.geoss.curated.workflow.service.WorkflowService;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
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
public class WorkflowServiceImpl implements WorkflowService {

    private static final String REALM_NAME = "geoss";

    private static final String ADMIN_GROUP_NAME = "administrator";

    private final UserResourceService userResourceService;

    private final UserRelationService userRelationService;

    private final UserExtensionService userExtensionService;

    private final UserDashboardService userDashboardService;

    private final ElasticsearchService elasticsearchService;

    private final EmailEventPublisher emailEventPublisher;

    private final Keycloak keycloak;

    /**
     * Instantiates a new Workflow service.
     *
     * @param userResourceService the user resource service
     * @param userRelationService the user relation service
     * @param userExtensionService the user extension service
     * @param elasticsearchService the elasticsearch service
     * @param emailEventPublisher the email event publisher
     * @param keycloak the keycloak
     */
    public WorkflowServiceImpl(UserResourceService userResourceService, UserRelationService userRelationService,
            UserExtensionService userExtensionService, UserDashboardService userDashboardService,
            ElasticsearchService elasticsearchService, EmailEventPublisher emailEventPublisher, Keycloak keycloak) {
        this.userResourceService = userResourceService;
        this.userRelationService = userRelationService;
        this.userExtensionService = userExtensionService;
        this.userDashboardService = userDashboardService;
        this.elasticsearchService = elasticsearchService;
        this.emailEventPublisher = emailEventPublisher;
        this.keycloak = keycloak;
    }

    @Transactional
    @Override
    public void approveUserResource(long userResourceId, String host) {
        log.info("Workflow approving user resource with id {}", userResourceId);
        UserResource userResource = userResourceService.approveUserResource(userResourceId);
        elasticsearchService.indexEntry(userResource.getEntry());
        emailEventPublisher.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userResourceService.findUserResource(userResourceId).getUserId())
                        .toRepresentation().getEmail(),
                Locale.getDefault(),
                "resource.approved.title",
                "emails/workflow-resource-approved.html",
                Map.of(
                        "id", userResourceId,
                        "name", userResourceService.findUserResource(userResourceId).getEntryName(),
                        "resourceUrl", host + "/resources/" + userResourceId
                ));
        log.info("Workflow approved user resource.");
    }

    @Transactional
    @Override
    public void denyUserResource(long userResourceId, String host) {
        log.info("Workflow denying user resource with id {}", userResourceId);
        userResourceService.denyUserResource(userResourceId);
        emailEventPublisher.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userResourceService.findUserResource(userResourceId).getUserId())
                        .toRepresentation().getEmail(),
                Locale.getDefault(),
                "resource.denied.title",
                "emails/workflow-resource-denied.html",
                Map.of(
                        "id", userResourceId,
                        "name", userResourceService.findUserResource(userResourceId).getEntryName(),
                        "resourceUrl", host + "/resources/" + userResourceId
                ));
        log.info("Workflow denied user resource.");
    }

    @Transactional
    @Override
    public void pendingUserResource(long userResourceId, String host) {
        log.info("Workflow pending user resource with id {}", userResourceId);
        userResourceService.pendingUserResource(userResourceId);
        Optional<GroupRepresentation> groupByName = keycloak.realm(REALM_NAME).groups().groups().stream()
                .filter(groupRepresentation -> groupRepresentation.getName().equals(ADMIN_GROUP_NAME)).findAny();
        GroupRepresentation group =
                groupByName.orElseThrow(() -> new NotFoundException("Group not found with name " + ADMIN_GROUP_NAME));
        List<UserRepresentation> users = keycloak.realm(REALM_NAME).groups().group(group.getId()).members();
        users.forEach(userRepresentation ->
                emailEventPublisher.send(
                        userRepresentation.getEmail(),
                        Locale.getDefault(),
                        "resource.pending.title",
                        "emails/workflow-resource-pending.html",
                        Map.of(
                                "id", userResourceId,
                                "name", userResourceService.findUserResource(userResourceId).getEntryName(),
                                "resourcesVerifyUrl", host + "/resources/verify/" + userResourceId
                        )));
        log.info("Workflow pending user resource.");
    }

    @Transactional
    @Override
    public void deleteUserResource(long userResourceId, String host) {
        log.info("Workflow delete user resource with id {}", userResourceId);
        UserResource userResource = userResourceService.findUserResource(userResourceId);
        elasticsearchService.removeEntryFromIndex(userResource.getEntry());
        Map<String, Object> variables = Map.of(
                "id", userResourceId,
                "name", userResourceService.findUserResource(userResourceId).getEntryName()
        );
        String userId = userResourceService.findUserResource(userResourceId).getUserId();
        userResourceService.deleteUserResource(userResourceId);
        emailEventPublisher.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userId)
                        .toRepresentation().getEmail(), Locale.getDefault(),
                "resource.deleted.title", "emails/workflow-resource-deleted.html", variables);
        log.info("Workflow delete user resource.");
    }

    @Transactional
    @Override
    public void approveUserRelation(long userRelationId, String host) {
        log.info("Workflow approving user relation with id {}", userRelationId);
        UserRelation userRelation = userRelationService.approveUserResource(userRelationId);
        elasticsearchService.indexEntryRelation(userRelation.getEntryRelation());
        emailEventPublisher.send(
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
        emailEventPublisher.send(
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
                emailEventPublisher.send(
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
        emailEventPublisher.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userId)
                        .toRepresentation().getEmail(), Locale.getDefault(),
                "relation.deleted.title", "emails/workflow-relation-deleted.html", variables);
        log.info("Workflow delete user relation.");
    }

    @Transactional
    @Override
    public void pendingUserExtension(long userExtensionId, String host) {
        log.info("Workflow pending user extension with id {}", userExtensionId);
        userExtensionService.pendingUserExtension(userExtensionId);
        Optional<GroupRepresentation> groupByName = keycloak.realm(REALM_NAME).groups().groups().stream()
                .filter(groupRepresentation -> groupRepresentation.getName().equals(ADMIN_GROUP_NAME)).findAny();
        GroupRepresentation group =
                groupByName.orElseThrow(() -> new NotFoundException("Group not found with name " + ADMIN_GROUP_NAME));
        List<UserRepresentation> users = keycloak.realm(REALM_NAME).groups().group(group.getId()).members();
        users.forEach(userRepresentation ->
                emailEventPublisher.send(
                        userRepresentation.getEmail(),
                        Locale.getDefault(),
                        "extension.pending.title",
                        "emails/workflow-extension-pending.html",
                        Map.of(
                                "id", userExtensionId,
                                "name", userExtensionService.findUserExtension(userExtensionId).getEntryName(),
                                "extensionsVerifyUrl", host + "/extensions/verify/" + userExtensionId
                        )));
        log.info("Workflow pending user extension.");
    }

    @Transactional
    @Override
    public void approveUserExtension(long userExtensionId, String host) {
        log.info("Workflow approving user extension with id {}", userExtensionId);
        UserExtension userExtension = userExtensionService.approveUserExtension(userExtensionId);
        emailEventPublisher.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userExtensionService.findUserExtension(userExtensionId).getUserId())
                        .toRepresentation().getEmail(),
                Locale.getDefault(),
                "extension.approved.title",
                "emails/workflow-extension-approved.html",
                Map.of(
                        "id", userExtensionId,
                        "name", userExtensionService.findUserExtension(userExtensionId).getEntryName(),
                        "extensionUrl", host + "/extensions/" + userExtensionId
                ));
        log.info("Workflow approved user resource.");
    }

    @Transactional
    @Override
    public void denyUserExtension(long userExtensionId, String host) {
        log.info("Workflow denying user extension with id {}", userExtensionId);
        userExtensionService.denyUserExtension(userExtensionId);
        emailEventPublisher.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userExtensionService.findUserExtension(userExtensionId).getUserId())
                        .toRepresentation().getEmail(),
                Locale.getDefault(),
                "extension.denied.title",
                "emails/workflow-extension-denied.html",
                Map.of(
                        "id", userExtensionId,
                        "name", userExtensionService.findUserExtension(userExtensionId).getEntryName(),
                        "extensionUrl", host + "/extensions/" + userExtensionId
                ));
        log.info("Workflow denied user extension.");
    }

    @Transactional
    @Override
    public void deleteUserExtension(long userExtensionId, String host) {
        log.info("Workflow delete user extension with id {}", userExtensionId);
        UserExtension userExtension = userExtensionService.findUserExtension(userExtensionId);
        Map<String, Object> variables = Map.of(
                "id", userExtensionId,
                "name", userExtensionService.findUserExtension(userExtensionId).getEntryName()
        );
        String userId = userExtensionService.findUserExtension(userExtensionId).getUserId();
        userExtensionService.deleteUserExtension(userExtensionId);
        emailEventPublisher.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userId)
                        .toRepresentation().getEmail(), Locale.getDefault(),
                "extension.deleted.title", "emails/workflow-extension-deleted.html", variables);
        log.info("Workflow delete user extension.");
    }

    @Transactional
    @Override
    public void pendingUserDashboard(long userDashboardId, String host) {
        log.info("Workflow pending user dashboard with id {}", userDashboardId);
        userDashboardService.pendingUserDashboard(userDashboardId);
        Optional<GroupRepresentation> groupByName = keycloak.realm(REALM_NAME).groups().groups().stream()
                .filter(groupRepresentation -> groupRepresentation.getName().equals(ADMIN_GROUP_NAME)).findAny();
        GroupRepresentation group =
                groupByName.orElseThrow(() -> new NotFoundException("Group not found with name " + ADMIN_GROUP_NAME));
        List<UserRepresentation> users = keycloak.realm(REALM_NAME).groups().group(group.getId()).members();
        users.forEach(userRepresentation ->
                emailEventPublisher.send(
                        userRepresentation.getEmail(),
                        Locale.getDefault(),
                        "dashboard.pending.title",
                        "emails/workflow-dashboard-pending.html",
                        Map.of(
                                "id", userDashboardId,
                                "name", userDashboardService.findUserDashboard(userDashboardId).getEntryName(),
                                "dashboardsVerifyUrl", host + "/dashboards/verify/" + userDashboardId
                        )));
        log.info("Workflow pending user dashboard.");
    }

    @Transactional
    @Override
    public void approveUserDashboard(long userDashboardId, String host) {
        log.info("Workflow approving user dashboard with id {}", userDashboardId);
        UserDashboard userDashboard = userDashboardService.approveUserDashboard(userDashboardId);
        emailEventPublisher.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userDashboardService.findUserDashboard(userDashboardId).getUserId())
                        .toRepresentation().getEmail(),
                Locale.getDefault(),
                "dashboard.approved.title",
                "emails/workflow-dashboard-approved.html",
                Map.of(
                        "id", userDashboardId,
                        "name", userDashboardService.findUserDashboard(userDashboardId).getEntryName(),
                        "dashboardUrl", host + "/dashboards/" + userDashboardId
                ));
        log.info("Workflow approved user dashboard.");
    }

    @Transactional
    @Override
    public void denyUserDashboard(long userDashboardId, String host) {
        log.info("Workflow denying user dashboard with id {}", userDashboardId);
        userDashboardService.denyUserDashboard(userDashboardId);
        emailEventPublisher.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userDashboardService.findUserDashboard(userDashboardId).getUserId())
                        .toRepresentation().getEmail(),
                Locale.getDefault(),
                "dashboard.denied.title",
                "emails/workflow-dashboard-denied.html",
                Map.of(
                        "id", userDashboardId,
                        "name", userDashboardService.findUserDashboard(userDashboardId).getEntryName(),
                        "dashboardUrl", host + "/dashboards/" + userDashboardId
                ));
        log.info("Workflow denied user dashboard.");
    }

    @Transactional
    @Override
    public void deleteUserDashboard(long userDashboardId, String host) {
        log.info("Workflow delete user dashboard with id {}", userDashboardId);
        UserDashboard userDashboard = userDashboardService.findUserDashboard(userDashboardId);
        Map<String, Object> variables = Map.of(
                "id", userDashboardId,
                "name", userDashboardService.findUserDashboard(userDashboardId).getEntryName()
        );
        String userId = userDashboardService.findUserDashboard(userDashboardId).getUserId();
        userDashboardService.deleteUserDashboard(userDashboardId);
        emailEventPublisher.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userId)
                        .toRepresentation().getEmail(), Locale.getDefault(),
                "dashboard.deleted.title", "emails/workflow-dashboard-deleted.html", variables);
        log.info("Workflow delete user dashboard.");
    }

}
