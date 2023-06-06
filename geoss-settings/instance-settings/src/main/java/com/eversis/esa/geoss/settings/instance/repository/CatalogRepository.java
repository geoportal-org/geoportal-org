package com.eversis.esa.geoss.settings.instance.repository;

import com.eversis.esa.geoss.settings.instance.domain.Catalog;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Optional;

/**
 * The interface Catalog repository.
 */
@RepositoryRestResource(path = "catalogs", collectionResourceRel = "catalogs", itemResourceRel = "catalog")
@Tag(name = "catalogs")
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    /**
     * Find by dab id optional.
     *
     * @param dabId the dab id
     * @return the optional
     */
    @RestResource(exported = false)
    @EntityGraph(attributePaths = "subOptions")
    Optional<Catalog> findByDabId(@Param("dabId") String dabId);
}
