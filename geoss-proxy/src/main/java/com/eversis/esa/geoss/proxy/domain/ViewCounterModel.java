package com.eversis.esa.geoss.proxy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * The type View counter model.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ViewCounterModel extends LoggedModel {

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
