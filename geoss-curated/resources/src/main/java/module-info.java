module geoss.curated.resources {
    exports com.eversis.esa.geoss.curated.resources.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires jakarta.persistence;
    requires spring.data.jpa;
    requires spring.data.rest.core;
    requires spring.data.commons;
    requires org.hibernate.orm.core;
    requires spring.web;
    requires spring.data.rest.webmvc;
    requires org.apache.logging.log4j;
    requires io.swagger.v3.oas.annotations;
    requires jakarta.validation;
    requires spring.tx;
    requires com.fasterxml.jackson.annotation;
    requires org.springdoc.openapi.common;
    requires io.swagger.v3.oas.models;
    requires spring.core;
    requires spring.beans;
    requires spring.security.core;
}
