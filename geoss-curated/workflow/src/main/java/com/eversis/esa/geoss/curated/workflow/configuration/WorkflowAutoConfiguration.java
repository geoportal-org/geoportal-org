package com.eversis.esa.geoss.curated.workflow.configuration;

import com.eversis.esa.geoss.curated.workflow.properties.KeycloakProperties;
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
        prefix = "geoss.curated.workflow", name = "enabled", havingValue = "true"
)
@EnableConfigurationProperties(
        value = {
                KeycloakProperties.class,
                WorkflowProperties.class
        }
)
@Import(WorkflowConfiguration.class)
public class WorkflowAutoConfiguration {

}
