package com.eversis.esa.geoss.settings.common.event;

import com.eversis.esa.geoss.settings.common.domain.RevisionInfo;

import lombok.extern.log4j.Log4j2;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * The type Revision info revision listener.
 */
@Log4j2
public class RevisionInfoRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        RevisionInfo revisionInfo = (RevisionInfo) revisionEntity;

        String username = Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .orElse("unknown");

        revisionInfo.setUsername(username);
        log.info("revisionEntity:{}", revisionInfo);
    }
}
