package com.eversis.esa.geoss.personaldata.survey.event;

import com.eversis.esa.geoss.common.domain.AuditableEmbeddable;
import com.eversis.esa.geoss.personaldata.survey.domain.Survey;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.auditing.AuditingHandler;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;

/**
 * The type Survey auditing entity listener.
 */
@Configurable
public class SurveyAuditingEntityListener {

    private ObjectFactory<AuditingHandler> auditingHandlerObjectFactory;

    /**
     * Sets auditing handler.
     *
     * @param auditingHandlerObjectFactory the auditing handler object factory
     */
    @Autowired
    public void setAuditingHandler(ObjectFactory<AuditingHandler> auditingHandlerObjectFactory) {
        this.auditingHandlerObjectFactory = auditingHandlerObjectFactory;
    }

    /**
     * Touch for create.
     *
     * @param survey the survey
     */
    @PrePersist
    public void touchForCreate(Survey survey) {
        if (auditingHandlerObjectFactory != null) {
            AuditingHandler auditingHandler = auditingHandlerObjectFactory.getObject();
            synchronized (SurveyAuditingEntityListener.class) {
                AuditableEmbeddable auditable = survey.getAuditable();
                if (auditable.getCreatedDate() != null) {
                    auditingHandler.setDateTimeForNow(false);
                    if (auditable.getLastModifiedDate() == null) {
                        auditable.setLastModifiedDate(Instant.now());
                    }
                }
                auditingHandler.markCreated(survey);
                auditingHandler.setDateTimeForNow(true);
            }
        }
    }

    /**
     * Touch for update.
     *
     * @param survey the survey
     */
    @PreUpdate
    public void touchForUpdate(Survey survey) {
        if (auditingHandlerObjectFactory != null) {
            AuditingHandler auditingHandler = auditingHandlerObjectFactory.getObject();
            synchronized (SurveyAuditingEntityListener.class) {
                auditingHandler.setDateTimeForNow(true);
                auditingHandler.markModified(survey);
            }
        }
    }
}
