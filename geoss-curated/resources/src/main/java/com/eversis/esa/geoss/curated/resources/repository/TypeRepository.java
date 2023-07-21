package com.eversis.esa.geoss.curated.resources.repository;

import java.util.List;
import java.util.Optional;

import com.eversis.esa.geoss.curated.resources.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Type repository.
 */
@RepositoryRestResource(exported = false)
public interface TypeRepository extends JpaRepository<Type, Long> {

    /**
     * Find by code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<Type> findByCode(String code);

    /**
     * Find by is custom order by name list.
     *
     * @param isCustom the is custom
     * @return the list
     */
    List<Type> findByIsCustomOrderByName(int isCustom);

}
