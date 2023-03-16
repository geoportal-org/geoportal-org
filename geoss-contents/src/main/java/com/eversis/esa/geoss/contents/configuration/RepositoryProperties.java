package com.eversis.esa.geoss.contents.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Repository properties.
 */
@ConfigurationProperties(prefix = "repository.upload")
@Data
public class RepositoryProperties {

    private String directory;

}
