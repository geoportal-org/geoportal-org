module geoss.system.settings {
    exports com.eversis.esa.geoss.settings.system.domain;
    exports com.eversis.esa.geoss.settings.system.repository;

    exports com.eversis.esa.geoss.settings.system.configuration to spring.beans, spring.boot, spring.context;
    // exports com.eversis.esa.geoss.settings.system.domain to spring.beans, spring.boot, spring.context;
    // exports com.eversis.esa.geoss.settings.system.repository to spring.beans, spring.boot, spring.context;
    // exports com.eversis.esa.geoss.settings.system.event to spring.beans, spring.context;

    requires com.fasterxml.jackson.annotation;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires lombok;
    requires org.apache.logging.log4j;
    requires org.hibernate.orm.envers;
    requires org.springdoc.openapi.common;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.context.support;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.data.rest.core;
    requires spring.hateoas;
    requires spring.tx;
    requires spring.web;
    requires spring.webmvc;
}
