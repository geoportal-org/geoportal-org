package com.eversis.esa.geoss.settings.common.support;

import com.eversis.esa.geoss.common.domain.AuditableEmbeddable;
import com.eversis.esa.geoss.settings.common.model.AuditableModel;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * The type Auditable embeddable to auditable model.
 */
@Component
public class AuditableEmbeddableToAuditableModelConverter implements Converter<AuditableEmbeddable, AuditableModel> {

    @Override
    public AuditableModel convert(AuditableEmbeddable auditableEmbeddable) {
        AuditableModel auditableModel = new AuditableModel();
        auditableModel.setCreatedBy(auditableEmbeddable.getCreatedBy());
        auditableModel.setCreatedDate(auditableEmbeddable.getCreatedDate());
        auditableModel.setLastModifiedBy(auditableEmbeddable.getLastModifiedBy());
        auditableModel.setLastModifiedDate(auditableEmbeddable.getLastModifiedDate());
        return auditableModel;
    }
}
