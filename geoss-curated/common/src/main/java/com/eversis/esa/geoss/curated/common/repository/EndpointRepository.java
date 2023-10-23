package com.eversis.esa.geoss.curated.common.repository;

import com.eversis.esa.geoss.curated.common.domain.Endpoint;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

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
     * @param pageable the pageable
     * @return the list
     */
    List<Endpoint> findByIsCustomOrderByUrl(int isCustom, Pageable pageable);

    /**
     * Find endpoint url type values list.
     *
     * @return the list
     */
    @Query("SELECT DISTINCT e.urlType FROM Endpoint e")
    List<String> findEndpointUrlTypeValues();

    /**
     * Find endpoints list.
     *
     * @param query the query
     * @param pageable the pageable
     * @return the list
     */
    @Query("SELECT e FROM Endpoint e WHERE e.url like %:query%")
    List<Endpoint> findEndpoints(String query, Pageable pageable);

}
