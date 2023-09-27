package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.AccessPolicy;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Coverage;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DefinitionType;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.EntryRelation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Organisation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.RelationType;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Source;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Type;

import lombok.NonNull;

import jakarta.validation.constraints.NotNull;

/**
 * The type Base sdg mapper.
 */
abstract class BaseSdgMapper {

    private final Organisation unitedNationsStatDivision;
    private final Source unitedNationsSource;
    private final String entryCodePrefix;

    /**
     * Instantiates a new Base sdg mapper.
     *
     * @param unitedNationsStatDivision the united nations stat division
     * @param unitedNationsSource the united nations source
     * @param entryCodePrefix the entry code prefix
     */
    public BaseSdgMapper(Organisation unitedNationsStatDivision,
            Source unitedNationsSource, String entryCodePrefix) {
        this.unitedNationsStatDivision = unitedNationsStatDivision;
        this.unitedNationsSource = unitedNationsSource;
        this.entryCodePrefix = entryCodePrefix;
    }

    protected Entry createSdgEntry(String code, String title, String summary, Type type) {
        Entry entry = new Entry(generateEntryCode(code), title, type, DefinitionType.PREDEFINED,
                DataSource.GEOSS_CURATED, DataSource.GEOSS_CURATED);
        entry.setSummary(summary);
        entry.setOrganisation(unitedNationsStatDivision);
        entry.setSource(unitedNationsSource);
        entry.setCoverage(Coverage.WORLD.getCoordinates());
        entry.setAccessPolicy(AccessPolicy.OPEN);
        return entry;
    }

    /**
     * Create sdg entry relation entry relation.
     *
     * @param parentCode the parent code
     * @param parentType the parent type
     * @param descendantCode the descendant code
     * @param descendantType the descendant type
     * @return the entry relation
     */
    public EntryRelation createSdgEntryRelation(
            @NonNull String parentCode,
            @NotNull Type parentType,
            @NonNull String descendantCode,
            @NotNull Type descendantType) {
        return new EntryRelation(
                generateEntryCode(parentCode), DataSource.GEOSS_CURATED, parentType,
                generateEntryCode(descendantCode), DataSource.GEOSS_CURATED, descendantType,
                RelationType.PARENT);
    }

    protected String generateEntryCode(String code) {
        return entryCodePrefix + code;
    }

}
