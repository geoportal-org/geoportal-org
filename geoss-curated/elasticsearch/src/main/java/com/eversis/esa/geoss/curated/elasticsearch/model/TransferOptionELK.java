package com.eversis.esa.geoss.curated.elasticsearch.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Transfer option elk.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferOptionELK implements Serializable {

    private String id;

    private String name;

    private String description;

    private String title;

    private String protocol;

    private String url;

    private String urlType;

}
