module geoss.curated.extensions {
    exports com.eversis.esa.geoss.curated.extensions.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires io.swagger.v3.oas.annotations;
    requires jakarta.annotation;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.apache.logging.log4j;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.data.rest.core;
    requires spring.data.rest.webmvc;
    requires spring.security.core;
    requires spring.tx;
    requires spring.web;
    requires geoss.curated.common;
}
