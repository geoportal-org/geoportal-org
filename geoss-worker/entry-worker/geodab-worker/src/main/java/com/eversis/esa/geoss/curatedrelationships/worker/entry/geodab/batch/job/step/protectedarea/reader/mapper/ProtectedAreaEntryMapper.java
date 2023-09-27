package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.reader.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.EntryRelation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Organisation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Source;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.DabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.GeoDabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.constants.EntryIdentifiers;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.constants.EntryTypes;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.mapper.BaseGeodabMapper;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.reader.dto.ProtectedArea;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Protected area entry mapper.
 */
@Component
public class ProtectedAreaEntryMapper extends BaseGeodabMapper {

    private final Organisation geodabOrganisation;
    private final ProtectedAreaTransferOptionFactory transferOptionFactory;

    /**
     * Instantiates a new Protected area entry mapper.
     *
     * @param dabProperties the dab configuration
     * @param geoDabProperties the geo dab configuration
     * @param transferOptionFactory the transfer option factory
     */
    @Autowired
    public ProtectedAreaEntryMapper(
            DabProperties dabProperties,
            GeoDabProperties geoDabProperties,
            ProtectedAreaTransferOptionFactory transferOptionFactory) {
        super(new Source(dabProperties.getSourceTitle(), dabProperties.getSourceCode()),
                dabProperties.getEntryCodePrefix());
        this.geodabOrganisation = new Organisation(geoDabProperties.getOrganisation());
        this.transferOptionFactory = transferOptionFactory;
    }

    /**
     * Create ecosystem entry entry.
     *
     * @param protectedArea the protected area
     * @param ecosystemId the ecosystem id
     * @return the entry
     */
    public Entry createEcosystemEntry(@NonNull ProtectedArea protectedArea, @NonNull String ecosystemId) {
        Entry entry = createEcosystemEntry(protectedArea);

        EntryRelation ecosystemProtectedAreaRelation = createEntryRelationUsingGeneratedCodes(
                ecosystemId, DataSource.GEOSS_CURATED, EntryTypes.ECOSYSTEM_TYPE, protectedArea.getId(),
                DataSource.GEOSS_CURATED, entry.getType());
        entry.addRelation(ecosystemProtectedAreaRelation);

        TransferOption transferOption = transferOptionFactory.createTransferOption(protectedArea);
        entry.addTransferOption(transferOption);

        return entry;
    }

    /**
     * Create ecosystem entry entry.
     *
     * @param protectedArea the protected area
     * @return the entry
     */
    public Entry createEcosystemEntry(@NonNull ProtectedArea protectedArea) {
        Entry entry = createEntry(
                generateDefaultEntryCode(protectedArea.getId()),
                protectedArea.getName(),
                "Lat:" + protectedArea.getLat() + " Lon:" + protectedArea.getLon(),
                EntryTypes.PROTECTED_AREA_TYPE
        );
        entry.setLogo(protectedArea.getImageUrl());
        entry.setKeywords(Stream.of(EntryIdentifiers.PROTECTED_AREA).collect(Collectors.toSet()));
        entry.setTags(Stream.of(EntryIdentifiers.PROTECTED_AREA.toLowerCase()).collect(Collectors.toSet()));
        entry.setOrganisation(geodabOrganisation);
        return entry;
    }

}
