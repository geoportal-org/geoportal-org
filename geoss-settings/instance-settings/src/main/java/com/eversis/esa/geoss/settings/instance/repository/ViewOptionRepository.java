package com.eversis.esa.geoss.settings.instance.repository;

import com.eversis.esa.geoss.settings.instance.domain.ViewOption;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.stream.Stream;

/**
 * The interface View option repository.
 */
@RepositoryRestResource(exported = false)
@Tag(name = "views")
public interface ViewOptionRepository extends JpaRepository<ViewOption, Long> {

    /**
     * Find by view id stream.
     *
     * @param viewId the view id
     * @return the stream
     */
    Stream<ViewOption> findByViewId(Long viewId);

    /**
     * Delete all by view id long.
     *
     * @param viewId the view id
     * @return the long
     */
    long deleteAllByViewId(Long viewId);
}
