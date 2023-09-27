package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.config;

import com.zaxxer.hikari.HikariConfigMXBean;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.JmxUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import javax.sql.DataSource;

/**
 * The type Data source configuration.
 */
@Log4j2
@Configuration
class DataSourceConfiguration {

    /**
     * Categories data source data source.
     *
     * @return the data source
     */
    @Bean("categoriesDataSource")
    DataSource categoriesDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db-schema-categories.sql")
                .build();
    }

    /**
     * Data source data source.
     *
     * @param dataSourceProperties the data source properties
     * @return the data source
     */
    @Primary
    @BatchDataSource
    @Bean("geossCuratedDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    DataSource dataSource(
            @Qualifier("spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties")
            DataSourceProperties dataSourceProperties) {
        HikariDataSource hikariDataSource = dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
        if (StringUtils.hasText(dataSourceProperties.getName())) {
            hikariDataSource.setPoolName(dataSourceProperties.getName());
        }
        return hikariDataSource;
    }

    /**
     * Named parameter jdbc template named parameter jdbc template.
     *
     * @param dataSource the data source
     * @return the named parameter jdbc template
     */
    @Bean("geossCuratedNamedParameterJdbcTemplate")
    NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("geossCuratedDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * The type Data source jmx configuration.
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(prefix = "spring.jmx", name = "enabled", havingValue = "true", matchIfMissing = true)
    static class DataSourceJmxConfiguration {

        /**
         * The type Hikari.
         */
        @Configuration(proxyBeanMethods = false)
        @ConditionalOnClass(HikariDataSource.class)
        static class Hikari implements ApplicationContextAware, InitializingBean {

            private ApplicationContext applicationContext;

            @Override
            public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
                this.applicationContext = applicationContext;
            }

            @Override
            public void afterPropertiesSet() {
                final Map<String, DataSource> dataSources = applicationContext.getBeansOfType(DataSource.class);
                final Collection<MBeanExporter> mBeanExporters = applicationContext.getBeansOfType(MBeanExporter.class)
                        .values();

                dataSources.forEach((beanName, dataSource) -> {
                    HikariDataSource hikariDataSource = DataSourceUnwrapper.unwrap(dataSource, HikariConfigMXBean.class,
                            HikariDataSource.class);
                    if (hikariDataSource != null && hikariDataSource.isRegisterMbeans()) {
                        for (MBeanExporter mbeanExporter : mBeanExporters) {
                            if (JmxUtils.isMBean(hikariDataSource.getClass())) {
                                mbeanExporter.addExcludedBean(beanName);
                            }
                        }
                    }
                });
            }
        }
    }
}
