package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.EntryGarbageRepository;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DefinitionType;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl.EntryGarbageLogSqlQueries.CODE_PREFIX_PARAM;
import static com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl.EntryGarbageLogSqlQueries.DEFINITION_TYPE_CODE_PARAM;
import static com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl.EntryGarbageLogSqlQueries.DELETED_PARAM;
import static com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl.EntryGarbageLogSqlQueries.DELETE_NO_LONGER_VALID_COUNTERS_QUERY;
import static com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl.EntryGarbageLogSqlQueries.LAST_ENTRY_UPDATE_PARAM;
import static com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl.EntryGarbageLogSqlQueries.LAST_GARBAGE_LOG_UPDATE_PARAM;
import static com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl.EntryGarbageLogSqlQueries.MARK_AS_ACTIVE_ENTRIES_MISSING_FROM_GARBAGE_COUNTER_QUERY;
import static com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl.EntryGarbageLogSqlQueries.MARK_AS_DELETED_ENTRIES_EXCEEDING_GARBAGE_COUNTER_LIMIT_QUERY;
import static com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl.EntryGarbageLogSqlQueries.NOT_FOUND_COUNTER_LIMIT_PARAM;
import static com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl.EntryGarbageLogSqlQueries.SELECT_COUNT_NOT_MODIFIED_ENTRIES_QUERY;
import static com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl.EntryGarbageLogSqlQueries.UPDATE_DELETED_STATUS_OF_ENTRY_RELATIONS_QUERY;
import static com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl.EntryGarbageLogSqlQueries.UPDATE_NOT_FOUND_COUNTERS_QUERY;

/**
 * The type Entry garbage repository.
 */
@Log4j2
@Repository
public class EntryGarbageRepositoryImpl implements EntryGarbageRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Instantiates a new Entry garbage repository.
     *
     * @param namedParameterJdbcTemplate the named parameter jdbc template
     */
    public EntryGarbageRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Gets not updated entries count.
     *
     * @param codePrefix the code prefix
     * @param definitionType the definition type
     * @param lastEntryUpdate the last entry update
     * @return the not updated entries count
     */
    @Override
    public int getNotUpdatedEntriesCount(String codePrefix, DefinitionType definitionType,
            LocalDateTime lastEntryUpdate) {
        try {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue(CODE_PREFIX_PARAM, codePrefix + "%")
                    .addValue(DEFINITION_TYPE_CODE_PARAM, definitionType.getCode())
                    .addValue(LAST_ENTRY_UPDATE_PARAM, lastEntryUpdate.format(DateTimeFormatter.ISO_DATE_TIME));
            Integer count = namedParameterJdbcTemplate.queryForObject(
                    SELECT_COUNT_NOT_MODIFIED_ENTRIES_QUERY,
                    parameterSource,
                    Integer.class);
            return count != null ? count : 0;
        } catch (EmptyResultDataAccessException e) {
            log.debug("No records found which were not modified using provided parameters - codePrefix: {}"
                    + ", definitionType: {}", codePrefix, definitionType);
            return 0;
        }
    }

    /**
     * Update not found counters.
     *
     * @param codePrefix the code prefix
     * @param definitionType the definition type
     * @param lastEntryUpdate the last entry update
     */
    @Override
    public void updateNotFoundCounters(String codePrefix, DefinitionType definitionType,
            LocalDateTime lastEntryUpdate) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(CODE_PREFIX_PARAM, codePrefix + "%")
                .addValue(DEFINITION_TYPE_CODE_PARAM, definitionType.getCode())
                .addValue(LAST_ENTRY_UPDATE_PARAM, lastEntryUpdate.format(DateTimeFormatter.ISO_DATE_TIME));

        int affectedRows = namedParameterJdbcTemplate.update(
                UPDATE_NOT_FOUND_COUNTERS_QUERY,
                parameterSource);
        log.debug("Affected rows during updating not found counters : {}", affectedRows);
    }

    /**
     * Remove no longer valid garbage logs int.
     *
     * @param codePrefix the code prefix
     * @param definitionType the definition type
     * @param lastEntryUpdate the last entry update
     * @param lastGarbageLogUpdate the last garbage log update
     * @return the int
     */
    @Override
    public int removeNoLongerValidGarbageLogs(
            String codePrefix, DefinitionType definitionType, LocalDateTime lastEntryUpdate,
            LocalDateTime lastGarbageLogUpdate) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue(CODE_PREFIX_PARAM, codePrefix + "%")
                .addValue(DEFINITION_TYPE_CODE_PARAM, definitionType.getCode())
                .addValue(LAST_ENTRY_UPDATE_PARAM, lastEntryUpdate.format(DateTimeFormatter.ISO_DATE_TIME))
                .addValue(LAST_GARBAGE_LOG_UPDATE_PARAM, lastGarbageLogUpdate.format(DateTimeFormatter.ISO_DATE_TIME));
        int affectedRows = namedParameterJdbcTemplate.update(
                DELETE_NO_LONGER_VALID_COUNTERS_QUERY,
                parameterSource);
        log.debug("Affected rows during removing no longer valid garbage logs counters : {}", affectedRows);
        return affectedRows;
    }

    /**
     * Remove entities exceeding not found limit.
     *
     * @param codePrefix the code prefix
     * @param definitionType the definition type
     * @param notFoundLimit the not found limit
     */
    @Transactional
    @Override
    public void removeEntitiesExceedingNotFoundLimit(String codePrefix, DefinitionType definitionType,
            int notFoundLimit) {
        SqlParameterSource markEntryAsRemovedParamSource = new MapSqlParameterSource()
                .addValue(CODE_PREFIX_PARAM, codePrefix + "%")
                .addValue(DEFINITION_TYPE_CODE_PARAM, definitionType.getCode())
                .addValue(NOT_FOUND_COUNTER_LIMIT_PARAM, notFoundLimit);
        int affectedRowsByMarkEntryAsRemoved = namedParameterJdbcTemplate.update(
                MARK_AS_DELETED_ENTRIES_EXCEEDING_GARBAGE_COUNTER_LIMIT_QUERY,
                markEntryAsRemovedParamSource);
        log.debug("Affected rows during removing entries exceeding garbage log counter : {}",
                affectedRowsByMarkEntryAsRemoved);

        SqlParameterSource markAsActiveParamSource = new MapSqlParameterSource()
                .addValue(CODE_PREFIX_PARAM, codePrefix + "%")
                .addValue(DEFINITION_TYPE_CODE_PARAM, definitionType.getCode());
        int affectedRowsByMarkEntryAsActive = namedParameterJdbcTemplate.update(
                MARK_AS_ACTIVE_ENTRIES_MISSING_FROM_GARBAGE_COUNTER_QUERY,
                markAsActiveParamSource);
        log.debug("Affected rows during restoring entries : {}", affectedRowsByMarkEntryAsActive);

        SqlParameterSource markEntryRelationsAsActiveParamSource = new MapSqlParameterSource()
                .addValue(DELETED_PARAM, 0)
                .addValue(CODE_PREFIX_PARAM, codePrefix + "%")
                .addValue(DEFINITION_TYPE_CODE_PARAM, definitionType.getCode())
                .addValue(NOT_FOUND_COUNTER_LIMIT_PARAM, notFoundLimit);
        int affectedRowsByMarkEntryRelationAsActive = namedParameterJdbcTemplate.update(
                UPDATE_DELETED_STATUS_OF_ENTRY_RELATIONS_QUERY,
                markEntryRelationsAsActiveParamSource);
        log.debug("Affected rows during restoring entry relations : {}", affectedRowsByMarkEntryRelationAsActive);

        SqlParameterSource markEntryRelationsAsRemovedParamSource = new MapSqlParameterSource()
                .addValue(DELETED_PARAM, 1)
                .addValue(CODE_PREFIX_PARAM, codePrefix + "%")
                .addValue(DEFINITION_TYPE_CODE_PARAM, definitionType.getCode())
                .addValue(NOT_FOUND_COUNTER_LIMIT_PARAM, notFoundLimit);
        int affectedRowsByMarkEntryRelationAsRemoved = namedParameterJdbcTemplate.update(
                UPDATE_DELETED_STATUS_OF_ENTRY_RELATIONS_QUERY,
                markEntryRelationsAsRemovedParamSource);
        log.debug("Affected rows during removing entry relations : {}", affectedRowsByMarkEntryRelationAsRemoved);
    }
}
