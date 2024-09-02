package com.eversis.esa.geoss.curated.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Transfer option extension elk.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferOptionExtensionELK implements Serializable {

    private String id;

    private String name;

    private String description;

    private String title;

    private String protocol;

    private String url;

    private String urlType;

    private String entryExtensionId;

}
