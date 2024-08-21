package com.eversis.esa.geoss.curated.relations.repository;

import com.eversis.esa.geoss.curated.relations.domain.EntryRelation;
import com.eversis.esa.geoss.curated.relations.domain.EntryRelationId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * The interface Entry relation repository.
 */
@RepositoryRestResource(exported = false)
public interface EntryRelationRepository extends JpaRepository<EntryRelation, EntryRelationId> {

    /**
     * Find by id src id list.
     *
     * @param code the code
     * @return the list
     */
    List<EntryRelation> findById_SrcId(String code);

}
