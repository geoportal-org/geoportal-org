package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.PageImpl;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchDocumentMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchResponseMapper;

import lombok.extern.log4j.Log4j2;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The type Base elasticsearch response mapper.
 *
 * @param <T> the type parameter
 */
@Log4j2
abstract class BaseElasticsearchResponseMapper<T> implements ElasticsearchResponseMapper<T> {

    protected ElasticsearchDocumentMapper<T> documentMapper;

    /**
     * Instantiates a new Base elasticsearch response mapper.
     *
     * @param documentMapper the document mapper
     */
    public BaseElasticsearchResponseMapper(ElasticsearchDocumentMapper<T> documentMapper) {
        this.documentMapper = documentMapper;
    }

    @Override
    public Page<T> mapSearchResponse(SearchResponse searchResponse, Pageable pageable) {
        long totalHits = searchResponse.getHits().getTotalHits().value;
        List<T> elements = Arrays.stream(searchResponse.getHits().getHits())
                .map(this::mapSearchHit)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PageImpl<>(elements, pageable, totalHits);
    }

    private T mapSearchHit(SearchHit searchHit) {
        try {
            return documentMapper.mapToObject(searchHit);
        } catch (ElasticsearchParseException ex) {
            log.error("Could not parse search result: {}, ex: {}", searchHit, ex);
            return null;
        } catch (IOException ex) {
            throw new ElasticsearchException("Unable to process result from ES : ", ex);
        }
    }

}
