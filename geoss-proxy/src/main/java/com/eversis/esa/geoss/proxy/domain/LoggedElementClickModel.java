package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

/**
 * The type Logged element click model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedElementClickModel extends LoggedModel {

    /**
     * The Ui object id.
     */
    @NotNull
    String uiObjectId;

    /**
     * The Ui object class.
     */
    @NotNull
    String uiObjectClass;

    /**
     * The Ui source.
     */
    @NotNull
    String uiSource;

    /**
     * The Session site url.
     */
    String sessionSiteUrl;

    /**
     * The Ui entry id.
     */
    @NotNull
    String uiEntryId;

    /**
     * The Ui action.
     */
    @NotNull
    String uiAction;

    /**
     * The Ui label.
     */
    @NotNull
    String uiLabel; // never in use (?)

    /**
     * The Ui organisation.
     */
    @NotNull
    String uiOrganisation;

    /**
     * The Ui resource name.
     */
    @NotNull
    String uiResourceName;

    /**
     * The Operation.
     */
    String operation;

}
