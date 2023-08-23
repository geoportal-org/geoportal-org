package com.eversis.esa.geoss.curated.relations.repository;

import com.eversis.esa.geoss.curated.relations.domain.EntryRelation;
import com.eversis.esa.geoss.curated.relations.domain.EntryRelationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Entry relation repository.
 */
@RepositoryRestResource(exported = false)
public interface EntryRelationRepository extends JpaRepository<EntryRelation, EntryRelationId> {

}
