package com.eversis.esa.geoss.personaldata.profile.repository;

import com.eversis.esa.geoss.personaldata.profile.domain.Comment;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * The interface Comment repository.
 */
@RepositoryRestResource(path = "comments", collectionResourceRel = "comments", itemResourceRel = "comment")
@Tag(name = "comments")
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Find by current user page.
     *
     * @param pageable the pageable
     * @return the page
     */
    @RestResource(path = "current")
    @Query("select c from Comment c where c.user = ?#{authentication.name}")
    Page<Comment> findByCurrentUser(Pageable pageable);

    /**
     * Find by user page.
     *
     * @param user the user
     * @param pageable the pageable
     * @return the page
     */
    @RestResource(path = "byUser")
    Page<Comment> findByUser(String user, Pageable pageable);
}
