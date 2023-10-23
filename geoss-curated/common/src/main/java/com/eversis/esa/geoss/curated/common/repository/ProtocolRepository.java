package com.eversis.esa.geoss.curated.common.repository;

import com.eversis.esa.geoss.curated.common.domain.Protocol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * The interface Protocol repository.
 */
@RepositoryRestResource(exported = false)
public interface ProtocolRepository extends JpaRepository<Protocol, Long> {

    /**
     * Find by value optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<Protocol> findByValue(String code);

    /**
     * Find by is custom order by value list.
     *
     * @param isCustom the is custom
     * @return the list
     */
    List<Protocol> findByIsCustomOrderByValue(int isCustom);

}
