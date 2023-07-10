package com.eversis.esa.geoss.curated.recommendations.repository;

import com.eversis.esa.geoss.curated.recommendations.domain.DataSource;
import com.eversis.esa.geoss.curated.recommendations.domain.Recommendation;
import com.eversis.esa.geoss.curated.recommendations.domain.RecommendedEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * The interface Recommended entity repository.
 */
@RepositoryRestResource(exported = false)
public interface RecommendedEntityRepository extends JpaRepository<RecommendedEntity, Long> {

    /**
     * Exists by code and data source and recommendation boolean.
     *
     * @param code the code
     * @param dataSource the data source
     * @param recommendation the recommendation
     * @return the boolean
     */
    boolean existsByCodeAndDataSourceAndRecommendation(String code, DataSource dataSource,
            Recommendation recommendation);

    /**
     * Find by code and data source and recommendation optional.
     *
     * @param code the code
     * @param dataSource the data source
     * @param recommendation the recommendation
     * @return the optional
     */
    Optional<RecommendedEntity> findByCodeAndDataSourceAndRecommendation(String code, DataSource dataSource,
            Recommendation recommendation);
}
