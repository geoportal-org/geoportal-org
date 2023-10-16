package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.boot.jackson.JsonMixin;

/**
 * The type Throwable mixin.
 */
@JsonMixin(Throwable.class)
public abstract class ThrowableMixin {

    /**
     * Get stack trace stack trace element [ ].
     *
     * @return the stack trace element [ ]
     */
    @JsonIgnore
    public abstract StackTraceElement[] getStackTrace();
}
