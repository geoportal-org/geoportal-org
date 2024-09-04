package com.eversis.esa.geoss.common.event;

import com.eversis.esa.geoss.common.domain.RevisionInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.envers.RevisionListener;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * The type Revision info revision listener.
 */
@Log4j2
@RequiredArgsConstructor
@Configurable
public class RevisionInfoRevisionListener implements RevisionListener {

    private static final String DEFAULT_REVISION_USERNAME = "xxxxxxxx-xxxx-cron-xxxx-xxxxxxxxxxxx";

    private final ObjectProvider<AuditorAware<?>> auditorAware;

    @Override
    public void newRevision(Object revisionEntity) {
        RevisionInfo revisionInfo = (RevisionInfo) revisionEntity;

        String username = Optional.ofNullable(auditorAware.getIfAvailable())
                .flatMap(AuditorAware::getCurrentAuditor)
                .map(Object::toString)
                .orElse(DEFAULT_REVISION_USERNAME);

        revisionInfo.setUsername(username);
        log.debug("revisionEntity:{}", revisionInfo);
    }
}
