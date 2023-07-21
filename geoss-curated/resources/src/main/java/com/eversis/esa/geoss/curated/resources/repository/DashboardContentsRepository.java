package com.eversis.esa.geoss.curated.resources.repository;

import java.util.Optional;

import com.eversis.esa.geoss.curated.resources.domain.DashboardContents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Dashboard contents repository.
 */
@RepositoryRestResource(exported = false)
public interface DashboardContentsRepository extends JpaRepository<DashboardContents, Long> {

    /**
     * Find by content optional.
     *
     * @param content the content
     * @return the optional
     */
    Optional<DashboardContents> findByContent(String content);

}
