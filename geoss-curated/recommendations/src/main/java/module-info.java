module geoss.curated.recommendations {
    exports com.eversis.esa.geoss.curated.recommendations.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires static org.mapstruct;
    requires com.fasterxml.jackson.annotation;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
    requires jakarta.annotation;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.apache.logging.log4j;
    requires org.hibernate.orm.core;
    requires org.springdoc.openapi.common;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.core;
    requires spring.context;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.data.rest.core;
    requires spring.data.rest.webmvc;
    requires spring.hateoas;
    requires spring.security.core;
    requires spring.security.config;
    requires spring.tx;
    requires spring.web;
    requires geoss.common;
    requires geoss.curated.common;
}
