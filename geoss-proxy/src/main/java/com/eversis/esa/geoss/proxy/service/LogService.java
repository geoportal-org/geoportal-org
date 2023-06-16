package com.eversis.esa.geoss.proxy.service;

import com.eversis.esa.geoss.proxy.document.ElementClickDoc;
import com.eversis.esa.geoss.proxy.document.ResourceErrorDoc;
import com.eversis.esa.geoss.proxy.document.SearchResultDoc;
import com.eversis.esa.geoss.proxy.document.SignInDoc;
import com.eversis.esa.geoss.proxy.domain.LoggedElementClickModel;
import com.eversis.esa.geoss.proxy.domain.LoggedResourceErrorModel;
import com.eversis.esa.geoss.proxy.domain.LoggedSearchResultModel;
import com.eversis.esa.geoss.proxy.domain.LoggedSignInModel;

/**
 * The interface Log service.
 */
public interface LogService {

    /**
     * Log search result search result doc.
     *
     * @param loggedSearchResultModel the logged search result model
     * @return the search result doc
     */
    SearchResultDoc logSearchResult(LoggedSearchResultModel loggedSearchResultModel);

    /**
     * Log resource error resource error doc.
     *
     * @param loggedResourceErrorModel the logged resource error model
     * @return the resource error doc
     */
    ResourceErrorDoc logResourceError(LoggedResourceErrorModel loggedResourceErrorModel);

    /**
     * Log element click element click doc.
     *
     * @param loggedElementClickModel the logged element click model
     * @return the element click doc
     */
    ElementClickDoc logElementClick(LoggedElementClickModel loggedElementClickModel);

    /**
     * Log sign in sign in doc.
     *
     * @param loggedSignInModel the logged sign in model
     * @return the sign in doc
     */
    SignInDoc logSignIn(LoggedSignInModel loggedSignInModel);

}
