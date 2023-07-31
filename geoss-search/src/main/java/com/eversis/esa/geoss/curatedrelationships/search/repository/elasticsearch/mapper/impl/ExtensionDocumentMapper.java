package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryExtension;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryRelation;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryType;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Extension;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.TransferOptionExtension;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model.EntryExtensionELK;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model.EntryRelationELK;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model.ExtensionELK;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model.TransferOptionExtensionELK;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ExtensionDocumentMapper extends BaseElasticsearchDocumentMapper<Extension> {

    @Autowired
    public ExtensionDocumentMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Extension mapToObject(SearchHit searchHit) throws IOException {
        Map source = searchHit != null ? searchHit.getSourceAsMap() : Collections.emptyMap();
        ExtensionELK extensionDocument = objectMapper.convertValue(source, ExtensionELK.class);
        return mapExtensionDocument(extensionDocument);
    }

    private Extension mapExtensionDocument(ExtensionELK extensionELK) {
        if (extensionELK == null) {
            return null;
        }

        Extension extension = new Extension(extensionELK.getEntryCode(), DataSource.fromString(extensionELK.getDataSource()));
        extension.setEntryExtensions(mapEntryExtensions(extensionELK.getExtensions()));
        extension.setRelations(mapRelations(extensionELK.getRelations()));
        extension.setTransferOptions(mapExtendedTransferOptions(extensionELK.getTransferOptions()));
        return extension;
    }

    private Set<EntryExtension> mapEntryExtensions(List<EntryExtensionELK> entryExtensionElasticsearchList) {
        if (CollectionUtils.isEmpty(entryExtensionElasticsearchList)) {
            return Collections.emptySet();
        }

        return entryExtensionElasticsearchList.stream()
                .map(entryExtensionELK -> EntryExtension.builder()
                        .entryExtensionId(entryExtensionELK.getEntryExtensionId())
                        .summary(entryExtensionELK.getSummary())
                        .keywords(mapKeywords(entryExtensionELK.getKeywords()))
                        .username(entryExtensionELK.getUsername())
                        .userId(entryExtensionELK.getUserId())
                        .createdDate(entryExtensionELK.getCreatedDate())
                        .modifiedDate(entryExtensionELK.getModifiedDate())
                        .build())
                .collect(Collectors.toSet());
    }

    private Set<String> mapKeywords(List<String> elasticsearchKeywords) {
        if (CollectionUtils.isEmpty(elasticsearchKeywords)) {
            return Collections.emptySet();
        }

        return new HashSet<>(elasticsearchKeywords);
    }

    private Set<EntryRelation> mapRelations(List<EntryRelationELK> entryRelationElasticsearchList) {
        if (CollectionUtils.isEmpty(entryRelationElasticsearchList)) {
            return Collections.emptySet();
        }

        return entryRelationElasticsearchList.stream()
                .map(entryRelationELK -> EntryRelation.builder()
                        .srcDataSource(DataSource.fromString(entryRelationELK.getSrcDataSource()))
                        .srcEntryCode(entryRelationELK.getSrcEntryCode())
                        .srcType(EntryType.fromString(entryRelationELK.getSrcEntryType()))
                        .destDataSource(DataSource.fromString(entryRelationELK.getDestDataSource()))
                        .destEntryCode(entryRelationELK.getDestEntryCode())
                        .destType(EntryType.fromString(entryRelationELK.getDestEntryType()))
                        .relationType(entryRelationELK.getRelationType())
                        .modifiedDate(entryRelationELK.getModifiedDate())
                        .createdDate(entryRelationELK.getCreatedDate())
                        .build()
                )
                .collect(Collectors.toSet());
    }

    private Set<TransferOptionExtension> mapExtendedTransferOptions(List<TransferOptionExtensionELK> transferOptionExtensionElasticsearchList) {
        if (CollectionUtils.isEmpty(transferOptionExtensionElasticsearchList)) {
            return Collections.emptySet();
        }

        return transferOptionExtensionElasticsearchList.stream()
                .map(transferOptionExtensionELK -> TransferOptionExtension.builder()
                        .entryExtensionId(transferOptionExtensionELK.getEntryExtensionId())
                        .name(transferOptionExtensionELK.getName())
                        .displayTitle(transferOptionExtensionELK.getTitle())
                        .description(transferOptionExtensionELK.getDescription())
                        .protocol(transferOptionExtensionELK.getProtocol())
                        .url(transferOptionExtensionELK.getUrl())
                        .urlType(transferOptionExtensionELK.getUrlType())
                        .build())
                .collect(Collectors.toSet());

    }
}
