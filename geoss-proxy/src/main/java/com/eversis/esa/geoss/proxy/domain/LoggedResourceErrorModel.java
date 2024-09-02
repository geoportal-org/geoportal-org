package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

/**
 * The type Logged resource error model.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoggedResourceErrorModel extends LoggedModel {

    /**
     * The Ui entry id.
     */
    @NotNull
    String uiEntryId;

    /**
     * The Result.
     */
    @NotNull
    String result;

    /**
     * The Operation.
     */
    @NotNull
    String operation;

    /**
     * The Message.
     */
    @NotNull
    String message;

    /**
     * The Result details.
     */
    @NotNull
    String resultDetails;

    /**
     * The Session site url.
     */
    String sessionSiteUrl;

    /**
     * The Short url.
     */
    String shortUrl;

}
