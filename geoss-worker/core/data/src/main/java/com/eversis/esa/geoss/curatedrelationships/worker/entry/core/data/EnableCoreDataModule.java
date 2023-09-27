package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Enable core data module.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(CoreDataModuleConfiguration.class)
@Configuration
public @interface EnableCoreDataModule {

}
