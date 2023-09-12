package com.eversis.esa.geoss.curated.common.repository;

import com.eversis.esa.geoss.curated.common.domain.Type;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

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
