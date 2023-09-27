package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.reader.mapper;

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
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.reader.dto.Storyline;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Storyline entry mapper.
 */
@Component
public class StorylineEntryMapper extends BaseGeodabMapper {

    private final Organisation geodabOrganisation;
    private final StorylineTransferOptionFactory transferOptionFactory;

    /**
     * Instantiates a new Storyline entry mapper.
     *
     * @param dabProperties the dab configuration
     * @param geoDabProperties the geo dab configuration
     * @param transferOptionFactory the transfer option factory
     */
    @Autowired
    public StorylineEntryMapper(
            DabProperties dabProperties,
            GeoDabProperties geoDabProperties,
            StorylineTransferOptionFactory transferOptionFactory) {
        super(new Source(dabProperties.getSourceTitle(), dabProperties.getSourceCode()),
                dabProperties.getEntryCodePrefix());
        this.geodabOrganisation = new Organisation(geoDabProperties.getOrganisation());
        this.transferOptionFactory = transferOptionFactory;
    }

    /**
     * Create storyline entry entry.
     *
     * @param storyline the storyline
     * @param protectedAreaId the protected area id
     * @return the entry
     */
    public Entry createStorylineEntry(@NonNull Storyline storyline, @NonNull String protectedAreaId) {
        Entry entry = createStorylineEntry(storyline);

        EntryRelation protectedAreaStorylineRelation = createEntryRelationUsingGeneratedCodes(
                protectedAreaId, DataSource.GEOSS_CURATED, EntryTypes.PROTECTED_AREA_TYPE,
                storyline.getId(), DataSource.GEOSS_CURATED, entry.getType());
        entry.addRelation(protectedAreaStorylineRelation);

        TransferOption transferOption = transferOptionFactory.createTransferOption(storyline);
        entry.addTransferOption(transferOption);

        return entry;
    }

    /**
     * Create storyline entry entry.
     *
     * @param storyline the storyline
     * @return the entry
     */
    public Entry createStorylineEntry(@NonNull Storyline storyline) {
        Entry entry = createEntry(
                generateDefaultEntryCode(storyline.getId()),
                storyline.getName(),
                storyline.getDescription(),
                EntryTypes.STORYLINE_TYPE
        );
        entry.setKeywords(Stream.of(EntryIdentifiers.STORYLINE).collect(Collectors.toSet()));
        entry.setTags(Stream.of(EntryIdentifiers.STORYLINE.toLowerCase()).collect(Collectors.toSet()));
        entry.setOrganisation(geodabOrganisation);
        return entry;
    }

}
