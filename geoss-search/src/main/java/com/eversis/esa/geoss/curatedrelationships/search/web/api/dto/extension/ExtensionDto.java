package com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.extension;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryRelation;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.TransferOptionExtension;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class ExtensionDto implements Serializable {

    private final String code;
    private final DataSource dataSource;

    private Set<String> dataHubTypes = new HashSet<>();
    private Set<EntryRelation> relations = new HashSet<>();
    private Set<EntryExtensionDto> entryExtensions = new HashSet<>();
    private Set<TransferOptionExtension> transferOptions = new HashSet<>();

}
