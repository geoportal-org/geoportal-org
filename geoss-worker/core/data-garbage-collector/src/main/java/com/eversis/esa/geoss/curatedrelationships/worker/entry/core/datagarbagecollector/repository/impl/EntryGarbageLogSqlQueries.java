package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.impl;

/**
 * The type Entry garbage log sql queries.
 */
class EntryGarbageLogSqlQueries {

    /**
     * The Code prefix param.
     */
    static final String CODE_PREFIX_PARAM = "codePrefix";
    /**
     * The Definition type code param.
     */
    static final String DEFINITION_TYPE_CODE_PARAM = "definitionTypeCode";
    /**
     * The Last entry update param.
     */
    static final String LAST_ENTRY_UPDATE_PARAM = "lastEntryUpdate";
    /**
     * The Last garbage log update param.
     */
    static final String LAST_GARBAGE_LOG_UPDATE_PARAM = "lastGarbageLogUpdate";
    /**
     * The Not found counter limit param.
     */
    static final String NOT_FOUND_COUNTER_LIMIT_PARAM = "notFoundCounterLimit";
    /**
     * The Deleted param.
     */
    static final String DELETED_PARAM = "deleted";

    /**
     * The Select count not modified entries query.
     */
    static final String SELECT_COUNT_NOT_MODIFIED_ENTRIES_QUERY = ""
            + "  SELECT COUNT(entry.id) "
            + "    FROM  entry "
            + "    JOIN  definitiontype ON entry.definitionTypeId = definitiontype.id"
            + "   WHERE  entry.modifiedDate < :" + LAST_ENTRY_UPDATE_PARAM
            + "          AND definitiontype.code = :" + DEFINITION_TYPE_CODE_PARAM
            + "          AND entry.code LIKE :" + CODE_PREFIX_PARAM;

    /**
     * The Update not found counters query.
     */
    static final String UPDATE_NOT_FOUND_COUNTERS_QUERY = ""
            + " INSERT INTO entrygarbagelog (entryId, notFoundCounter)"
            + "  SELECT "
            + "       entry.id AS entryId, "
            + "       1 AS notFoundCounter"
            + "    FROM  entry "
            + "    JOIN  definitiontype ON entry.definitionTypeId = definitiontype.id"
            + "   WHERE  entry.modifiedDate < :" + LAST_ENTRY_UPDATE_PARAM
            + "          AND definitiontype.code = :" + DEFINITION_TYPE_CODE_PARAM
            + "          AND entry.code LIKE :" + CODE_PREFIX_PARAM
            + " ON DUPLICATE KEY UPDATE notFoundCounter = (notFoundCounter + 1)";

    /**
     * The Delete no longer valid counters query.
     */
    static final String DELETE_NO_LONGER_VALID_COUNTERS_QUERY = ""
            + " DELETE entrygarbagelog"
            + " FROM entrygarbagelog"
            + " JOIN entry ON entrygarbagelog.entryId = entry.id"
            + " JOIN definitiontype ON entry.definitionTypeId = definitiontype.id"
            + " WHERE entry.modifiedDate > :" + LAST_ENTRY_UPDATE_PARAM
            + "      AND definitiontype.code = :" + DEFINITION_TYPE_CODE_PARAM
            + "      AND entry.code LIKE :" + CODE_PREFIX_PARAM
            + "      AND entrygarbagelog.modifiedDate < :" + LAST_GARBAGE_LOG_UPDATE_PARAM;

    /**
     * The Mark as deleted entries exceeding garbage counter limit query.
     */
    static final String MARK_AS_DELETED_ENTRIES_EXCEEDING_GARBAGE_COUNTER_LIMIT_QUERY = ""
            + " UPDATE entry "
            + " JOIN entrygarbagelog ON entrygarbagelog.entryId = entry.id  "
            + " JOIN definitiontype ON entry.definitionTypeId = definitiontype.id"
            + " SET entry.deleted = TRUE"
            + " WHERE "
            + "      definitiontype.code = :" + DEFINITION_TYPE_CODE_PARAM
            + "      AND entry.code LIKE :" + CODE_PREFIX_PARAM
            + "      AND entrygarbagelog.notFoundCounter >= :" + NOT_FOUND_COUNTER_LIMIT_PARAM;

    /**
     * The Mark as active entries missing from garbage counter query.
     */
    static final String MARK_AS_ACTIVE_ENTRIES_MISSING_FROM_GARBAGE_COUNTER_QUERY = ""
            + " UPDATE entry "
            + " JOIN definitiontype ON entry.definitionTypeId = definitiontype.id"
            + " SET entry.deleted = FALSE"
            + " WHERE "
            + "      definitiontype.code = :" + DEFINITION_TYPE_CODE_PARAM
            + "      AND entry.code LIKE :" + CODE_PREFIX_PARAM
            + "      AND NOT EXISTS("
            + "          SELECT entrygarbagelog.entryId FROM entrygarbagelog WHERE entrygarbagelog.entryId = entry.id"
            + "      )";

    /**
     * The Update deleted status of entry relations query.
     */
    static final String UPDATE_DELETED_STATUS_OF_ENTRY_RELATIONS_QUERY = ""
            + " UPDATE entryrelation "
            + " JOIN entry ON "
            + "     (entryrelation.srcId = entry.code AND entryrelation.srcDataSourceId = entry.dataSourceId)"
            + "     OR (entryrelation.destId = entry.code AND entryrelation.destDataSourceId = entry.dataSourceId)"
            + " JOIN definitiontype ON entry.definitionTypeId = definitiontype.id"
            + " SET  entryrelation.deleted = :" + DELETED_PARAM
            + " WHERE "
            + "      definitiontype.code = :" + DEFINITION_TYPE_CODE_PARAM
            + "      AND entry.code LIKE :" + CODE_PREFIX_PARAM
            + "      AND entry.deleted = :" + DELETED_PARAM;

}
