package com.eversis.esa.geoss.settings.system.repository;

import com.eversis.esa.geoss.settings.system.domain.PortalSetupWizard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Portal setup wizard repository.
 */
@RepositoryRestResource(collectionResourceRel = "portalSetupWizard", path = "portal-setup-wizard")
public interface PortalSetupWizardRepository extends JpaRepository<PortalSetupWizard, Long> {

}
