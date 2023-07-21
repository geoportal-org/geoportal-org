package com.eversis.esa.geoss.curated.resources.repository;

import java.util.List;
import java.util.Optional;

import com.eversis.esa.geoss.curated.resources.domain.AccessPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Access policy repository.
 */
@RepositoryRestResource(exported = false)
public interface AccessPolicyRepository extends JpaRepository<AccessPolicy, Long> {

    /**
     * Find by code optional.
     *
     * @param value the value
     * @return the optional
     */
    Optional<AccessPolicy> findByCode(String value);

    /**
     * Find by is custom order by name list.
     *
     * @param isCustom the is custom
     * @return the list
     */
    List<AccessPolicy> findByIsCustomOrderByName(int isCustom);

}
