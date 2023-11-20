package com.eversis.esa.geoss.curated.elasticsearch.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
