package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.AccessPolicy;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Coverage;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DefinitionType;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.EntryRelation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.RelationType;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Source;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Type;

import lombok.NonNull;

/**
 * The type Base geodab mapper.
 */
public abstract class BaseGeodabMapper {

    private final Source ecopotentialSource;
    private final String entryCodePrefix;

    /**
     * Instantiates a new Base geodab mapper.
     *
     * @param ecopotentialSource the ecopotential source
     * @param entryCodePrefix the entry code prefix
     */
    public BaseGeodabMapper(Source ecopotentialSource, String entryCodePrefix) {
        this.ecopotentialSource = ecopotentialSource;
        this.entryCodePrefix = entryCodePrefix;
    }

    protected Entry createEntry(String code, String title, String summary, Type type) {
        Entry entry = new Entry(code, title, type, DefinitionType.PREDEFINED, DataSource.GEOSS_CURATED,
                DataSource.GEOSS_CURATED);
        entry.setSummary(summary);
        entry.setSource(ecopotentialSource);
        entry.setCoverage(Coverage.WORLD.getCoordinates());
        entry.setAccessPolicy(AccessPolicy.OPEN);
        return entry;
    }

    /**
     * Create entry relation using generated codes entry relation.
     *
     * @param parentCode the parent code
     * @param parentDataSource the parent data source
     * @param parentType the parent type
     * @param descendantCode the descendant code
     * @param descendantDataSource the descendant data source
     * @param descendantType the descendant type
     * @return the entry relation
     */
    public EntryRelation createEntryRelationUsingGeneratedCodes(
            @NonNull String parentCode,
            @NonNull DataSource parentDataSource,
            @NonNull Type parentType,
            @NonNull String descendantCode,
            @NonNull DataSource descendantDataSource,
            @NonNull Type descendantType) {
        return new EntryRelation(
                generateDefaultEntryCode(parentCode), parentDataSource, parentType,
                generateDefaultEntryCode(descendantCode), descendantDataSource, descendantType,
                RelationType.PARENT);
    }

    /**
     * Create entry relation entry relation.
     *
     * @param parentCode the parent code
     * @param parentDataSource the parent data source
     * @param parentType the parent type
     * @param descendantCode the descendant code
     * @param descendantDataSource the descendant data source
     * @param descendantType the descendant type
     * @return the entry relation
     */
    public EntryRelation createEntryRelation(
            @NonNull String parentCode,
            @NonNull DataSource parentDataSource,
            @NonNull Type parentType,
            @NonNull String descendantCode,
            @NonNull DataSource descendantDataSource,
            @NonNull Type descendantType) {
        return new EntryRelation(parentCode, parentDataSource, parentType, descendantCode, descendantDataSource,
                descendantType, RelationType.PARENT);
    }

    protected String generateDefaultEntryCode(String code) {
        return generateEntryCode(entryCodePrefix, code);
    }

    protected String generateEntryCode(String prefix, String code) {
        return prefix + code;
    }

}
