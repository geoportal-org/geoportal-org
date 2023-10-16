package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model;

/**
 * Enumeration representing the status of an execution.
 */
public enum Status {

    /**
     * The batch job has successfully completed its execution.
     */
    COMPLETED,
    /**
     * Status of a batch job prior to its execution.
     */
    STARTING,
    /**
     * Status of a batch job that is running.
     */
    STARTED,
    /**
     * Status of batch job waiting for a step to complete before stopping the batch job.
     */
    STOPPING,
    /**
     * Status of a batch job that has been stopped by request.
     */
    STOPPED,
    /**
     * Status of a batch job that has failed during its execution.
     */
    FAILED,
    /**
     * Status of a batch job that did not stop properly and can not be restarted.
     */
    ABANDONED,
    /**
     * Status of a batch job that is in an uncertain state.
     */
    UNKNOWN;

    /**
     * Convenience method to decide if a status indicates that work is in progress.
     *
     * @return true if the status is STARTING, STARTED, STOPPING
     */
    public boolean isRunning() {
        return this == STARTING || this == STARTED || this == STOPPING;
    }

    /**
     * Convenience method to decide if a status indicates execution was unsuccessful.
     *
     * @return {@code true} if the status is {@code FAILED} or greater.
     */
    public boolean isUnsuccessful() {
        return this == FAILED || this == ABANDONED;
    }
}
