package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Extension;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchDocumentMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Extension response mapper.
 */
@Component
public class ExtensionResponseMapper extends BaseElasticsearchResponseMapper<Extension> {

    /**
     * Instantiates a new Extension response mapper.
     *
     * @param documentMapper the document mapper
     */
    @Autowired
    public ExtensionResponseMapper(ElasticsearchDocumentMapper<Extension> documentMapper) {
        super(documentMapper);
    }

}
