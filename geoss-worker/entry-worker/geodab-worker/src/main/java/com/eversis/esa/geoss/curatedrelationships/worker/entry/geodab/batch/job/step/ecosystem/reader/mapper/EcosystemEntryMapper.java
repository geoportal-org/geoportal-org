package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem.reader.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Organisation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Source;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.DabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.GeoDabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.constants.EntryTypes;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.mapper.BaseGeodabMapper;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem.reader.dto.Ecosystem;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Ecosystem entry mapper.
 */
@Component
public class EcosystemEntryMapper extends BaseGeodabMapper {

    private final Organisation geoDabOrganisation;

    /**
     * Instantiates a new Ecosystem entry mapper.
     *
     * @param dabProperties the dab configuration
     * @param geoDabProperties the geo dab configuration
     */
    @Autowired
    public EcosystemEntryMapper(DabProperties dabProperties, GeoDabProperties geoDabProperties) {
        super(new Source(dabProperties.getSourceTitle(), dabProperties.getSourceCode()),
                dabProperties.getEntryCodePrefix());
        this.geoDabOrganisation = new Organisation(geoDabProperties.getOrganisation());
    }

    /**
     * Create ecosystem entry entry.
     *
     * @param ecosystem the ecosystem
     * @return the entry
     */
    public Entry createEcosystemEntry(@NonNull Ecosystem ecosystem) {
        Entry entry = createEntry(
                generateDefaultEntryCode(ecosystem.getId()),
                ecosystem.getName(),
                ecosystem.getDescription(),
                EntryTypes.ECOSYSTEM_TYPE
        );
        entry.setLogo(ecosystem.getImageUrl());
        entry.setKeywords(Stream.of("Ecosystems").collect(Collectors.toSet()));
        entry.setTags(Stream.of("ecosystems").collect(Collectors.toSet()));
        entry.setOrganisation(geoDabOrganisation);
        return entry;
    }

}
