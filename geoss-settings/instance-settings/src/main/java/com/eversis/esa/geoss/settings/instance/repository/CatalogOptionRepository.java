package com.eversis.esa.geoss.settings.instance.repository;

import com.eversis.esa.geoss.settings.instance.domain.CatalogOption;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.stream.Stream;

/**
 * The interface Catalog option repository.
 */
@RepositoryRestResource(exported = false)
@Tag(name = "catalogs")
public interface CatalogOptionRepository extends JpaRepository<CatalogOption, Long> {

    /**
     * Find by catalog id stream.
     *
     * @param catalogId the catalog id
     * @return the stream
     */
    Stream<Object> findByCatalogId(Long catalogId);

    /**
     * Delete all by catalog id long.
     *
     * @param catalogId the catalog id
     * @return the long
     */
    long deleteAllByCatalogId(Long catalogId);
}
