package com.eversis.esa.geoss.curated.workflow.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Workflow properties.
 */
@Getter
@Setter
@ConfigurationProperties("geoss.curated.workflow")
public class WorkflowProperties {

    private boolean enabled;
}
