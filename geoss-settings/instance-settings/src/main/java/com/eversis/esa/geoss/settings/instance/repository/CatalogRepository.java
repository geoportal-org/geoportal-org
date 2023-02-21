package com.eversis.esa.geoss.settings.instance.repository;

import com.eversis.esa.geoss.settings.instance.domain.Catalog;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Catalog repository.
 */
@RepositoryRestResource(path = "catalogs", collectionResourceRel = "catalogs", itemResourceRel = "catalog")
@Tag(name = "catalogs")
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

}
