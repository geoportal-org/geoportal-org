package com.eversis.esa.geoss.proxy.domain;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

/**
 * The type Score model.
 */
@Data
public class ScoreModel extends LoggedModel {

    private String uiObjectId;
    private String uiObjectClass;
    private String uiSource;
    @NotNull
    private String sessionSiteUrl;
    @NotNull
    private String uiEntryId;
    @NotNull
    private String uiAction;
    private String uiLabel;  // never in use (?)
    @NotNull
    private String uiOrganisation;
    @NotNull
    private String uiResourceName;

}
