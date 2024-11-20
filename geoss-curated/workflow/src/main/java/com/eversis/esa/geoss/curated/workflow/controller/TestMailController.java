package com.eversis.esa.geoss.curated.workflow.controller;

import com.eversis.esa.geoss.common.domain.NonTransactionalSimpleMailMessage;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;
import java.util.List;

/**
 * The type Test mail controller.
 */
@Log4j2
@RequiredArgsConstructor
@Hidden
@Controller
@RequestMapping("/testmail")
public class TestMailController {

    private static final String REALM_NAME = "geoss";

    private final Keycloak keycloak;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Test send email.
     *
     * @param principal the principal
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void testSendEmail(Principal principal) {
        log.info("principal:{}", principal);
        UsersResource usersResource = keycloak.realm(REALM_NAME).users();
        List<UserRepresentation> userRepresentations = usersResource.search(principal.getName());
        for (UserRepresentation userRepresentation : userRepresentations) {
            if (principal.getName().equals(userRepresentation.getUsername())) {
                sendMail(userRepresentation.getEmail(), "[CURATED] test message", "This is a test message");
            }
        }
    }

    private void sendMail(String to, String subject, String body) {
        NonTransactionalSimpleMailMessage msg = new NonTransactionalSimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        applicationEventPublisher.publishEvent(msg);
    }
}
