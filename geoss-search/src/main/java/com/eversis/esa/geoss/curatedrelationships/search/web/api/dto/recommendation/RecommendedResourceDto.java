package com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.recommendation;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.recommendation.RecommendedResource;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecommendedResourceDto implements Serializable {

    private final DataSource dataSource;
    private final String code;
    private final String title;
    private final String description;

    public static RecommendedResourceDto of(RecommendedResource resource) {
        return new RecommendedResourceDto(
                resource.getDataSource(),
                resource.getCode(),
                resource.getName(),
                resource.getDescription());
    }
}
