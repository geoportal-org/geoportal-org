package com.eversis.esa.geoss.curated.workflow.configuration;

import com.eversis.esa.geoss.curated.workflow.properties.WorkflowProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Workflow auto configuration.
 */
@AutoConfiguration
@ConditionalOnProperty(
        prefix = "geoss.curated.workflow", name = "enabled", havingValue = "true", matchIfMissing = true
)
@EnableConfigurationProperties(
        value = {
                WorkflowProperties.class
        }
)
@Import(WorkflowConfiguration.class)
public class WorkflowAutoConfiguration {

}
