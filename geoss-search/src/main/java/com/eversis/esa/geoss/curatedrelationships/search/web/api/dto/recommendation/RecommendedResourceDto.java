package com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.recommendation;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.recommendation.RecommendedResource;

import lombok.Data;

import java.io.Serializable;

/**
 * The type Recommended resource dto.
 */
@Data
public class RecommendedResourceDto implements Serializable {

    private final DataSource dataSource;
    private final String code;
    private final String title;
    private final String description;

    /**
     * Of recommended resource dto.
     *
     * @param resource the resource
     * @return the recommended resource dto
     */
    public static RecommendedResourceDto of(RecommendedResource resource) {
        return new RecommendedResourceDto(
                resource.getDataSource(),
                resource.getCode(),
                resource.getName(),
                resource.getDescription());
    }
}
