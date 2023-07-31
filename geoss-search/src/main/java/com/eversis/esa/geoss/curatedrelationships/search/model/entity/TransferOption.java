package com.eversis.esa.geoss.curatedrelationships.search.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferOption implements Serializable {

    private String id;
    private String name;
    private String displayTitle;
    private String description;
    private String urlType;
    private String url;
    private String protocol;

}
