module geoss.settings.common {
    exports com.eversis.esa.geoss.settings.common.constraints;
    exports com.eversis.esa.geoss.settings.common.model;
    exports com.eversis.esa.geoss.settings.common.properties;
    exports com.eversis.esa.geoss.settings.common.support;
    exports com.eversis.esa.geoss.settings.common.web;

    exports com.eversis.esa.geoss.settings.common.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires geoss.common;
    requires jakarta.validation;
    requires org.apache.logging.log4j;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.data.rest.webmvc;
    requires spring.web;
}
