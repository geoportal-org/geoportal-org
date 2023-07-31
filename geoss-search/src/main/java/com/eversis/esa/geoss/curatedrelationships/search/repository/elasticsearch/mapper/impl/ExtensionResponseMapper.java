package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Extension;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchDocumentMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExtensionResponseMapper extends BaseElasticsearchResponseMapper<Extension> {

    @Autowired
    public ExtensionResponseMapper(ElasticsearchDocumentMapper<Extension> documentMapper) {
        super(documentMapper);
    }

}
