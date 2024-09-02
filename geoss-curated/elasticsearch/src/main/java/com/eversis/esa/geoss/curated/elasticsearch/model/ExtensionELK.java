package com.eversis.esa.geoss.curated.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Extension elk.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtensionELK implements Serializable {

    private long entryExtensionId;

    private String summary;

    private String username;

    private long userId;

    private List<String> keywords = new ArrayList<>();

    private LocalDateTime modifiedDate;

    private LocalDateTime createdDate;

}
