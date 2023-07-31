package com.eversis.esa.geoss.curatedrelationships.search.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * The type Dashboard contents.
 */
@Getter
@Setter
public class DashboardContents implements Serializable {

    private int id;
    /**
     * The Content.
     */
    String content;

}
