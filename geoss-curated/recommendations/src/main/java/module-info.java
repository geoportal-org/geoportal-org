module geoss.curated.recommendations {
    exports com.eversis.esa.geoss.curated.recommendations.domain;
    exports com.eversis.esa.geoss.curated.recommendations.model;
    exports com.eversis.esa.geoss.curated.recommendations.repository;
    exports com.eversis.esa.geoss.curated.recommendations.service;

    exports com.eversis.esa.geoss.curated.recommendations.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires static org.mapstruct;
    requires com.fasterxml.jackson.annotation;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
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
    requires spring.security.core;
    requires spring.tx;
    requires spring.web;
}
