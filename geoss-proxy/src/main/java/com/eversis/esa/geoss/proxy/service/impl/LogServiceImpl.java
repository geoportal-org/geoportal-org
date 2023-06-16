package com.eversis.esa.geoss.proxy.service.impl;

import com.eversis.esa.geoss.proxy.document.ElementClickDoc;
import com.eversis.esa.geoss.proxy.document.ResourceErrorDoc;
import com.eversis.esa.geoss.proxy.document.SearchResultDoc;
import com.eversis.esa.geoss.proxy.document.SignInDoc;
import com.eversis.esa.geoss.proxy.domain.LoggedElementClickModel;
import com.eversis.esa.geoss.proxy.domain.LoggedResourceErrorModel;
import com.eversis.esa.geoss.proxy.domain.LoggedSearchResultModel;
import com.eversis.esa.geoss.proxy.domain.LoggedSignInModel;
import com.eversis.esa.geoss.proxy.mapper.impl.ElementClickMapper;
import com.eversis.esa.geoss.proxy.mapper.impl.ResourceErrorMapper;
import com.eversis.esa.geoss.proxy.mapper.impl.SearchResultMapper;
import com.eversis.esa.geoss.proxy.mapper.impl.SignInMapper;
import com.eversis.esa.geoss.proxy.repository.ElementClickRepository;
import com.eversis.esa.geoss.proxy.repository.ResourceErrorRepository;
import com.eversis.esa.geoss.proxy.repository.SearchResultRepository;
import com.eversis.esa.geoss.proxy.repository.SignInRepository;
import com.eversis.esa.geoss.proxy.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The type Log service.
 */
@Slf4j
@Service
public class LogServiceImpl implements LogService {

    private final SearchResultRepository searchResultRepository;

    private final ResourceErrorRepository resourceErrorRepository;

    private final ElementClickRepository elementClickRepository;

    private final SignInRepository signInRepository;

    private final SearchResultMapper searchResultMapper;

    private final ResourceErrorMapper resourceErrorMapper;

    private final ElementClickMapper elementClickMapper;

    private final SignInMapper signInMapper;

    /**
     * Instantiates a new Log service.
     *
     * @param searchResultRepository the search result repository
     * @param resourceErrorRepository the resource error repository
     * @param elementClickRepository the element click repository
     * @param signInRepository the sign in repository
     * @param searchResultMapper the search result mapper
     * @param resourceErrorMapper the resource error mapper
     * @param elementClickMapper the element click mapper
     * @param signInMapper the sign in mapper
     */
    public LogServiceImpl(SearchResultRepository searchResultRepository,
            ResourceErrorRepository resourceErrorRepository,
            ElementClickRepository elementClickRepository, SignInRepository signInRepository,
            SearchResultMapper searchResultMapper, ResourceErrorMapper resourceErrorMapper,
            ElementClickMapper elementClickMapper, SignInMapper signInMapper) {
        this.searchResultRepository = searchResultRepository;
        this.resourceErrorRepository = resourceErrorRepository;
        this.elementClickRepository = elementClickRepository;
        this.signInRepository = signInRepository;
        this.searchResultMapper = searchResultMapper;
        this.resourceErrorMapper = resourceErrorMapper;
        this.elementClickMapper = elementClickMapper;
        this.signInMapper = signInMapper;
    }

    @Override
    public SearchResultDoc logSearchResult(LoggedSearchResultModel loggedSearchResultModel) {
        return searchResultRepository.save(searchResultMapper.mapToDocument(loggedSearchResultModel));
    }

    @Override
    public ResourceErrorDoc logResourceError(LoggedResourceErrorModel loggedResourceErrorModel) {
        return resourceErrorRepository.save(resourceErrorMapper.mapToDocument(loggedResourceErrorModel));
    }

    @Override
    public ElementClickDoc logElementClick(LoggedElementClickModel loggedElementClickModel) {
        return elementClickRepository.save(elementClickMapper.mapToDocument(loggedElementClickModel));
    }

    @Override
    public SignInDoc logSignIn(LoggedSignInModel loggedSignInModel) {
        return signInRepository.save(signInMapper.mapToDocument(loggedSignInModel));
    }

}
