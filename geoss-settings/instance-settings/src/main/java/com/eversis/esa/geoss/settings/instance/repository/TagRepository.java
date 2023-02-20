package com.eversis.esa.geoss.settings.instance.repository;

import com.eversis.esa.geoss.settings.instance.domain.Tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Tag repository.
 */
@RepositoryRestResource(path = "tags", collectionResourceRel = "tags", itemResourceRel = "tag")
@io.swagger.v3.oas.annotations.tags.Tag(name = "tutorial-tags")
public interface TagRepository extends JpaRepository<Tag, Long> {

}
