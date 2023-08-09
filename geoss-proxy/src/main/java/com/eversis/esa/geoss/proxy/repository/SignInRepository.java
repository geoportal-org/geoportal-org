package com.eversis.esa.geoss.proxy.repository;

import com.eversis.esa.geoss.proxy.document.SignInDoc;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * The interface Sign in repository.
 */
public interface SignInRepository extends ElasticsearchRepository<SignInDoc, String> {

}
