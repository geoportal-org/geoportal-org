package com.eversis.esa.geoss.curated.resources.repository;

import java.util.List;
import java.util.Optional;

import com.eversis.esa.geoss.curated.resources.domain.Endpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Endpoint repository.
 */
@RepositoryRestResource(exported = false)
public interface EndpointRepository extends JpaRepository<Endpoint, Long> {

    /**
     * Find by url and url type optional.
     *
     * @param url the url
     * @param urlType the url type
     * @return the optional
     */
    Optional<Endpoint> findByUrlAndUrlType(String url, String urlType);

    /**
     * Find by is custom order by url list.
     *
     * @param isCustom the is custom
     * @return the list
     */
    List<Endpoint> findByIsCustomOrderByUrl(int isCustom);

}
