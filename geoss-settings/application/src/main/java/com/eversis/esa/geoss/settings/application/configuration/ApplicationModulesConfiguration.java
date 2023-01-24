package com.eversis.esa.geoss.settings.application.configuration;

import com.eversis.esa.geoss.settings.common.configuration.CommonConfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The type Application modules configuration.
 */
@Import(CommonConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class ApplicationModulesConfiguration {

}
