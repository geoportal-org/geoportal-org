package com.eversis.esa.geoss.curated.resources.repository;

import java.util.List;
import java.util.Optional;

import com.eversis.esa.geoss.curated.resources.domain.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Organisation repository.
 */
@RepositoryRestResource(exported = false)
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {

    /**
     * Find by title optional.
     *
     * @param title the title
     * @return the optional
     */
    Optional<Organisation> findByTitle(String title);

    /**
     * Find by is custom order by title list.
     *
     * @param isCustom the is custom
     * @return the list
     */
    List<Organisation> findByIsCustomOrderByTitle(int isCustom);

}
