package com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.extension;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryRelation;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DetailedExtensionDto implements Serializable {

    private final String code;
    private final DataSource dataSourceType;
    private final List<DetailedEntryExtensionDto> extensions;
    private final List<EntryRelation> relations;

}
