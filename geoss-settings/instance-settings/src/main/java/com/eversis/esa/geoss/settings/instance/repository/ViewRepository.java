package com.eversis.esa.geoss.settings.instance.repository;

import com.eversis.esa.geoss.settings.instance.domain.View;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface View repository.
 */
@RepositoryRestResource(path = "views", collectionResourceRel = "views", itemResourceRel = "view")
@Tag(name = "views")
public interface ViewRepository extends JpaRepository<View, Long> {

}
