package com.eversis.esa.geoss.curated.resources.repository;

import com.eversis.esa.geoss.curated.resources.domain.EntryEvent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Entry event repository.
 */
@RepositoryRestResource(exported = false)
public interface EntryEventRepository extends JpaRepository<EntryEvent, Long> {

}
