module geoss.curated.resources {
    exports com.eversis.esa.geoss.curated.resources.configuration to spring.beans, spring.boot, spring.context;
    exports com.eversis.esa.geoss.curated.resources.domain;
    exports com.eversis.esa.geoss.curated.resources.model;
    exports com.eversis.esa.geoss.curated.resources.service;

    requires static lombok;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires jakarta.persistence;
    requires spring.data.jpa;
    requires org.hibernate.orm.core;
    requires com.fasterxml.jackson.annotation;
    requires geoss.curated.common;
    requires spring.data.commons;
    requires org.springdoc.openapi.common;
    requires io.swagger.v3.oas.models;
    requires spring.data.rest.webmvc;
    requires spring.web;
    requires io.swagger.v3.oas.annotations;
    requires spring.security.core;
    requires jakarta.validation;
    requires spring.tx;
    requires org.apache.logging.log4j;
    requires spring.data.rest.core;
    requires jakarta.annotation;
}
