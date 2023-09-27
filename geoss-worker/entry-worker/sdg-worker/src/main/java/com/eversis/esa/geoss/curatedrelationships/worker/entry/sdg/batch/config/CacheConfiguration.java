package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config;

import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Cache configuration.
 */
@EnableCaching
@Configuration
class CacheConfiguration {

    /**
     * Hibernate second level cache customizer hibernate properties customizer.
     *
     * @param cacheManager the cache manager
     * @return the hibernate properties customizer
     */
    @Bean
    public HibernatePropertiesCustomizer hibernateSecondLevelCacheCustomizer(JCacheCacheManager cacheManager) {
        return (properties) -> properties.put(ConfigSettings.CACHE_MANAGER, cacheManager.getCacheManager());
    }
}
