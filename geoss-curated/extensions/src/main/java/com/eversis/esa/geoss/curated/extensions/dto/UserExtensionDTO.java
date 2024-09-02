package com.eversis.esa.geoss.curated.extensions.dto;

import com.eversis.esa.geoss.curated.common.domain.Status;
import com.eversis.esa.geoss.curated.common.domain.TaskType;
import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The type User extension dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserExtensionDTO {

    private Long id;
    private String userId;
    private String entryName;
    private String description;
    private TaskType taskType;
    private EntryExtension entryExtension;
    private Status status;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private boolean hasOtherExtensionsWithSameEntry;

}
