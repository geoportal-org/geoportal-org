package com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.extension;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.TransferOptionExtension;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * The type Detailed entry extension dto.
 */
@Data
@EqualsAndHashCode(of = {"entryExtensionId"})
@Builder
public class DetailedEntryExtensionDto implements Serializable {

    private final Integer entryExtensionId;
    private String summary;
    private Set<String> keywords;

    private final String userName;
    private final String userId;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    private List<TransferOptionExtension> transferOptions;

}
