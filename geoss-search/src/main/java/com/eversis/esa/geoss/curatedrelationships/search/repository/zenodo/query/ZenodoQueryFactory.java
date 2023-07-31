package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.query;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class ZenodoQueryFactory {

    public String buildIdQuery(@NotNull String id) {
        return new ZenodoQueryBuilder()
                .id(id)
                .build();
    }

}
