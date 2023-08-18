package com.eversis.esa.geoss.curated.resources.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import com.eversis.esa.geoss.curated.common.email.EmailSender;
import com.eversis.esa.geoss.curated.resources.domain.UserResource;
import com.eversis.esa.geoss.curated.resources.elasticsearch.service.ElasticsearchService;
import com.eversis.esa.geoss.curated.resources.service.UserResourceService;
import com.eversis.esa.geoss.curated.resources.service.WorkflowService;

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

    private final ElasticsearchService elasticsearchService;

    private final EmailSender emailSender;

    private final Keycloak keycloak;

    /**
     * Instantiates a new Workflow service.
     *
     * @param userResourceService the user resource service
     * @param elasticsearchService the elasticsearch service
     * @param emailSender the email sender
     * @param keycloak the keycloak
     */
    public WorkflowServiceImpl(UserResourceService userResourceService, ElasticsearchService elasticsearchService,
            EmailSender emailSender, Keycloak keycloak) {
        this.userResourceService = userResourceService;
        this.elasticsearchService = elasticsearchService;
        this.emailSender = emailSender;
        this.keycloak = keycloak;
    }

    @Transactional
    @Override
    public void approveUserResource(long userResourceId, String host) {
        log.info("Workflow approving user resource with id {}", userResourceId);
        UserResource userResource = userResourceService.approveUserResource(userResourceId);
        elasticsearchService.indexEntry(userResource.getEntry());
        emailSender.send(
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
        emailSender.send(
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
                emailSender.send(
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
        emailSender.send(
                keycloak.realm(REALM_NAME).users()
                        .get(userId)
                        .toRepresentation().getEmail(), Locale.getDefault(),
                "resource.deleted.title", "emails/workflow-resource-deleted.html", variables);
        log.info("Workflow delete user resource.");
    }

}
