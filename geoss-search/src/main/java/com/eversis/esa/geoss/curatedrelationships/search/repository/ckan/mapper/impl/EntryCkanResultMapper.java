package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryType;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Keyword;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Organisation;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.DatasetCkan;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.DatasetResourceCkan;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.OrganizationCkan;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.TagCkan;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Entry ckan result mapper.
 */
@Component
class EntryCkanResultMapper extends BaseCkanResultMapper<Entry, Map> {

    private final TransferOptionCkanMapper transferOptionMapper;

    /**
     * Instantiates a new Entry ckan result mapper.
     *
     * @param objectMapper the object mapper
     * @param transferOptionMapper the transfer option mapper
     */
    @Autowired
    public EntryCkanResultMapper(
            ObjectMapper objectMapper,
            TransferOptionCkanMapper transferOptionMapper) {
        super(objectMapper);
        this.transferOptionMapper = transferOptionMapper;
    }

    @Override
    public Entry mapToObject(Map source) {
        Map sourceMap = source != null ? source : Collections.emptyMap();
        DatasetCkan dataset = objectMapper.convertValue(sourceMap, DatasetCkan.class);

        if (dataset == null) {
            return null;
        }

        return mapToResourceEntry(dataset);
    }

    private Entry mapToResourceEntry(DatasetCkan dataset) {
        return Entry.builder()
                .id(dataset.getId())
                .code(dataset.getId())
                .title(dataset.getTitle())
                .summary(dataset.getNotes())
                .logo("")
                .dataSource(DataSource.AMERIGEOSS_CKAN)
                .displayDataSource(DataSource.AMERIGEOSS_CKAN)
                .publishDate(dataset.getModifiedDate())
                .organisation(mapOrganisation(dataset.getOrganization(), dataset.getMaintainer(),
                        dataset.getMaintainerEmail()))
                .coverage(null) // TODO
                .isParent(false)  // TODO
                .childrenTypes(null) // TODO
                .parentIds(Collections.emptyList())
                .types(Collections.singletonList(EntryType.DATA))
                .keywords(mapKeywords(dataset.getTags()))
                .transferOptions(mapTransferOptions(dataset.getResources()))
                .build();
    }

    private Organisation mapOrganisation(OrganizationCkan organizationCkan, String maintainer, String maintainerEmail) {
        Organisation organisation = new Organisation();
        organisation.setId(organizationCkan.getId());
        organisation.setTitle(organizationCkan.getTitle());
        organisation.setContact(maintainer);
        organisation.setEmail(maintainerEmail);
        return organisation;
    }

    private List<Keyword> mapKeywords(List<TagCkan> tags) {
        if (tags == null) {
            return Collections.emptyList();
        }

        return tags.stream()
                .filter(tag -> "active".equals(tag.getName()))
                .map(TagCkan::getTitle)
                .map(Keyword::new)
                .collect(Collectors.toList());
    }

    private List<TransferOption> mapTransferOptions(List<DatasetResourceCkan> resources) {
        if (resources == null) {
            return Collections.emptyList();
        }

        return resources.stream()
                .map(transferOptionMapper::mapTransferOption)
                .collect(Collectors.toList());
    }
}
