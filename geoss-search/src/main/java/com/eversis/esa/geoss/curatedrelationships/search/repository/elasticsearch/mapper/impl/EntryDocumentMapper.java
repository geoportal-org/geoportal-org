package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryType;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Keyword;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Organisation;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model.OrganisationELK;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model.ResourceEntryELK;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model.TransferOptionELK;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The type Entry document mapper.
 */
@Component
public class EntryDocumentMapper extends BaseElasticsearchDocumentMapper<Entry> {

    private final TransferOptionElasticsearchMapper transferOptionMapper;

    /**
     * Instantiates a new Entry document mapper.
     *
     * @param objectMapper the object mapper
     * @param transferOptionMapper the transfer option mapper
     */
    @Autowired
    public EntryDocumentMapper(ObjectMapper objectMapper, TransferOptionElasticsearchMapper transferOptionMapper) {
        super(objectMapper);
        this.transferOptionMapper = transferOptionMapper;
    }

    @Override
    public Entry mapToObject(SearchHit searchHit) {
        Map source = searchHit != null ? searchHit.getSourceAsMap() : Collections.emptyMap();
        ResourceEntryELK entryELK = objectMapper.convertValue(source, ResourceEntryELK.class);
        if (entryELK != null && searchHit != null) {
            entryELK.setId(searchHit.getId());
        }

        return mapResourceEntry(entryELK);
    }

    private Entry mapResourceEntry(ResourceEntryELK entryELK) {
        if (entryELK == null) {
            return null;
        }
        Entry entry = new Entry();

        entry.setId(entryELK.getId());
        entry.setCode(entryELK.getCode());
        entry.setTitle(entryELK.getTitle());
        entry.setSummary(entryELK.getSummary());
        entry.setLogo(entryELK.getLogo());
        entry.setDashboardContents(entryELK.getDashboardContents());
        entry.setDataSource(DataSource.fromString(entryELK.getDataSource()));
        entry.setDisplayDataSource(DataSource.fromString(entryELK.getDisplayDataSource()));
        entry.setParent(entryELK.isParent());
        entry.setParentIds(entryELK.getParentIds());
        entry.setChildrenTypes(entryELK.getChildrenTypes());
        entry.setPublishDate(entryELK.getPublishDate());
        entry.setOrganisation(mapOrganisationELK(entryELK.getOrganisation()));
        entry.setCoverage(GeoShapeMapper.mapBoundingBoxELK(entryELK.getCoverage()));
        entry.setAccessPolicy(entryELK.getAccessPolicy());
        entry.setTypes(mapResourceTypesELK(entryELK.getTypes()));
        entry.setKeywords(mapKeywordsELK(entryELK.getKeywords()));
        entry.setTransferOptions(mapTransferOptions(entryELK.getTransferOptions()));
        return entry;
    }

    private List<EntryType> mapResourceTypesELK(List<String> resourceTypesELK) {
        if (resourceTypesELK == null) {
            return Collections.emptyList();
        }

        return resourceTypesELK.stream()
                .map(EntryType::fromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Organisation mapOrganisationELK(OrganisationELK organisationELK) {
        if (organisationELK == null) {
            return null;
        }

        Organisation organisation = new Organisation();
        organisation.setId(organisationELK.getId());
        organisation.setTitle(organisationELK.getTitle());
        organisation.setContact(organisationELK.getContact());
        organisation.setEmail(organisationELK.getEmail());
        organisation.setContactEmail(organisationELK.getContactEmail());
        organisation.setRole(organisationELK.getRole());
        return organisation;
    }

    private List<Keyword> mapKeywordsELK(List<String> keywordsELK) {
        if (keywordsELK == null) {
            return Collections.emptyList();
        }

        return keywordsELK.stream()
                .map(Keyword::new)
                .collect(Collectors.toList());
    }

    private List<TransferOption> mapTransferOptions(List<TransferOptionELK> transferOptionsELK) {
        if (transferOptionsELK == null) {
            return Collections.emptyList();
        }

        return transferOptionsELK.stream()
                .map(transferOptionMapper::mapTransferOption)
                .collect(Collectors.toList());
    }

}
