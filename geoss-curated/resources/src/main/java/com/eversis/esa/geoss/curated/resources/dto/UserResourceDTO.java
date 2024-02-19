package com.eversis.esa.geoss.curated.resources.dto;

import java.time.LocalDateTime;

import com.eversis.esa.geoss.curated.common.domain.Status;
import com.eversis.esa.geoss.curated.common.domain.TaskType;
import com.eversis.esa.geoss.curated.resources.domain.Entry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type User resource dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResourceDTO {

    private Long id;
    private String userId;
    private String entryName;
    private TaskType taskType;
    private Entry entry;
    private Status status;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;
    private boolean hasOtherEntriesWithSameEntry;

}
