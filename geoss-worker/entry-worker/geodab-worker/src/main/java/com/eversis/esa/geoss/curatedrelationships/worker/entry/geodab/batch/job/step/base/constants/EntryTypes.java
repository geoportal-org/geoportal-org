package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.constants;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Type;

/**
 * The type Entry types.
 */
public class EntryTypes {

    private EntryTypes() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The constant ECOSYSTEM_TYPE.
     */
    public static final Type ECOSYSTEM_TYPE = Type.INFORMATION;
    /**
     * The constant PROTECTED_AREA_TYPE.
     */
    public static final Type PROTECTED_AREA_TYPE = Type.INFORMATION;
    /**
     * The constant STORYLINE_TYPE.
     */
    public static final Type STORYLINE_TYPE = Type.INFORMATION;
    /**
     * The constant WORKFLOW_TYPE.
     */
    public static final Type WORKFLOW_TYPE = Type.SERVICE;
    /**
     * The constant WORKFLOW_OUTPUT_TYPE.
     */
    public static final Type WORKFLOW_OUTPUT_TYPE = Type.DATA;

}
