package com.eversis.esa.geoss.contents.repository;

import com.eversis.esa.geoss.contents.domain.Site;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Site repository.
 */
@RepositoryRestResource(collectionResourceRel = "site", path = "site")
@Tag(name = "sites")
public interface SiteRepository extends JpaRepository<Site, Long> {

    /**
     * Find by url site.
     *
     * @param url the url
     * @return the site
     */
    Site findByUrl(@Param("url") String url);

}
