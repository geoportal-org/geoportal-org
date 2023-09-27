package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Enable core data garbage collector module.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(CoreDataGarbageCollectorModuleConfiguration.class)
@Configuration
public @interface EnableCoreDataGarbageCollectorModule {

}
