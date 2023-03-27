package com.eversis.esa.geoss.personaldata.profile.repository;

import com.eversis.esa.geoss.personaldata.profile.domain.Comment;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * The interface Comment repository.
 */
@RepositoryRestResource(path = "comments", collectionResourceRel = "comments", itemResourceRel = "comment")
@Tag(name = "comments")
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Current user comments list.
     *
     * @return the list
     */
    @RestResource(path = "current")
    @Query("select c from Comment c where c.user = ?#{authentication.name}")
    List<Comment> currentUserComments();

    /**
     * Find comments by user list.
     *
     * @param user the user
     * @return the list
     */
    @RestResource(path = "byUser")
    List<Comment> findCommentsByUser(String user);
}
