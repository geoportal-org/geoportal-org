package com.eversis.esa.geoss.settings.instance.repository;

import com.eversis.esa.geoss.settings.instance.domain.Layer;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Layer repository.
 */
@RepositoryRestResource(path = "layers", collectionResourceRel = "layers", itemResourceRel = "layer")
@Tag(name = "default-layers")
public interface LayerRepository extends JpaRepository<Layer, Long> {

}
