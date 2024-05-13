module geoss.system.settings {
    exports com.eversis.esa.geoss.settings.system.domain;
    exports com.eversis.esa.geoss.settings.system.repository;
    exports com.eversis.esa.geoss.settings.system.service;

    exports com.eversis.esa.geoss.settings.system.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires static org.mapstruct;
    requires geoss.common;
    requires geoss.settings.common;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.module.jsonSchema.jakarta;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.apache.logging.log4j;
    requires org.hibernate.orm.core;
    requires org.hibernate.orm.envers;
    requires org.springdoc.openapi.common;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.data.rest.core;
    requires spring.data.rest.webmvc;
    requires spring.hateoas;
    requires spring.tx;
    requires spring.web;
    requires spring.webmvc;
}
