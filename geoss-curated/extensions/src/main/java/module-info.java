module geoss.curated.extensions {
    exports com.eversis.esa.geoss.curated.extensions.configuration to spring.beans, spring.boot, spring.context;
    exports com.eversis.esa.geoss.curated.extensions.domain;
    exports com.eversis.esa.geoss.curated.extensions.repository;

    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
    requires jakarta.annotation;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.apache.logging.log4j;
    requires org.springdoc.openapi.common;
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
    requires org.hibernate.orm.core;
}
