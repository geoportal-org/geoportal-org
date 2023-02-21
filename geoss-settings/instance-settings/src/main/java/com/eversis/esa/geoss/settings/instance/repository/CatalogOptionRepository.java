package com.eversis.esa.geoss.settings.instance.repository;

import com.eversis.esa.geoss.settings.instance.domain.CatalogOption;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Catalog option repository.
 */
@RepositoryRestResource(exported = false)
@Tag(name = "catalogs")
public interface CatalogOptionRepository extends JpaRepository<CatalogOption, Long> {

}
