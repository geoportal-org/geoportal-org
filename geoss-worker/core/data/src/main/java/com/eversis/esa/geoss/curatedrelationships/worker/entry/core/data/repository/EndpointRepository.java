package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Endpoint;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface Endpoint repository.
 */
public interface EndpointRepository extends JpaRepository<Endpoint, Integer> {

    /**
     * Find by url and url type optional.
     *
     * @param url the url
     * @param urlType the url type
     * @return the optional
     */
    Optional<Endpoint> findByUrlAndUrlType(String url, String urlType);

}
