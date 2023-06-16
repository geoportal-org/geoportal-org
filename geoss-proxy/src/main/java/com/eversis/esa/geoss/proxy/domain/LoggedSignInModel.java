package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Logged sign in model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedSignInModel extends LoggedModel {

    /**
     * The Session site url.
     */
    String sessionSiteUrl;

}
