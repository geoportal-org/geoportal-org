package com.eversis.esa.geoss.curated.resources.repository;

import com.eversis.esa.geoss.curated.resources.domain.BookmarkedResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Bookmarked result repository.
 */
@RepositoryRestResource(exported = false)
public interface BookmarkedResultRepository extends JpaRepository<BookmarkedResult, Long> {

    /**
     * Find by user id page.
     *
     * @param userId the user id
     * @param pageable the pageable
     * @return the page
     */
    Page<BookmarkedResult> findByUserId(String userId, Pageable pageable);

    /**
     * Delete by target id and data source.
     *
     * @param targetId the target id
     * @param dataSource the data source
     */
    void deleteByTargetIdAndDataSource(String targetId, String dataSource);

}
