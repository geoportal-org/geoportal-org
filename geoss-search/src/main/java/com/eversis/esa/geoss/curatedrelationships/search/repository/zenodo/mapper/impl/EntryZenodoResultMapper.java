package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryType;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Keyword;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Organisation;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.model.ZenodoCreator;
import com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.model.ZenodoResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Entry zenodo result mapper.
 */
@Component
class EntryZenodoResultMapper extends BaseZenodoResultMapper<Entry, ZenodoResult> {

    private final TransferOptionZenodoMapper transferOptionMapper;

    /**
     * Instantiates a new Entry zenodo result mapper.
     *
     * @param objectMapper the object mapper
     * @param transferOptionMapper the transfer option mapper
     */
    @Autowired
    EntryZenodoResultMapper(
            ObjectMapper objectMapper,
            TransferOptionZenodoMapper transferOptionMapper) {
        super(objectMapper);
        this.transferOptionMapper = transferOptionMapper;
    }

    @Override
    public Entry mapToObject(ZenodoResult result) throws IOException {
        if (result == null) {
            return null;
        }

        return mapToResourceEntry(result);
    }

    private Entry mapToResourceEntry(ZenodoResult result) {
        return Entry.builder()
                .id(result.getId())
                .code(result.getId())
                .title(result.getTitle())
                .summary(result.getDescription())
                .logo("")
                .dataSource(DataSource.ZENODO)
                .displayDataSource(DataSource.ZENODO)
                .publishDate(result.getCreated().toLocalDateTime())
                .organisation(mapOrganisation(result.getCreators()))
                .coverage(null) // TODO
                .isParent(false)  // TODO
                .childrenTypes(null) // TODO
                .parentIds(Collections.emptyList())
                .types(Collections.singletonList(EntryType.INFORMATION))
                .keywords(result.getKeywords().stream().map(Keyword::new).collect(Collectors.toList()))
                .transferOptions(mapTransferOptions(result))
                .build();
    }

    private Organisation mapOrganisation(List<ZenodoCreator> creators) {
        if (CollectionUtils.isEmpty(creators)) {
            return null;
        }

        Organisation organisation = new Organisation();
        organisation.setTitle(creators.stream()
                .map(ZenodoCreator::getAffiliation)
                .distinct()
                .collect(Collectors.joining(", ")));
        organisation.setContact(creators.stream()
                .map(ZenodoCreator::getName)
                .map(name -> name.replace(",", ""))
                .distinct()
                .collect(Collectors.joining(", ")));
        return organisation;
    }

    private List<TransferOption> mapTransferOptions(ZenodoResult result) {
        if (result == null) {
            return Collections.emptyList();
        }

        List<TransferOption> transferOptions = new ArrayList<>();

        transferOptions.add(
                transferOptionMapper.mapTransferOptionFromSelfLink(result.getTitle(), result.getHtmlPageLink()));
        result.getFiles().forEach(
                zenodoFile -> transferOptions.add(transferOptionMapper.mapTransferOptionFromSelfLink(zenodoFile)));

        return transferOptions;
    }
}
