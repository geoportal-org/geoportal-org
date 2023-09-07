package com.eversis.esa.geoss.curated.workflow.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Workflow configuration.
 */
@ComponentScan(
        basePackages = {
        }
)
@PropertySource("classpath:application-workflow.properties")
@Configuration(proxyBeanMethods = false)
public class WorkflowConfiguration {

}
