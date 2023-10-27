module geoss.settings.application {
    exports com.eversis.esa.geoss.settings.application;

    exports com.eversis.esa.geoss.settings.application.configuration to spring.beans, spring.boot, spring.context;

    opens com.eversis.esa.geoss.settings.application to spring.core;
    opens com.eversis.esa.geoss.settings.application.configuration to spring.core;

    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires com.zaxxer.hikari;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires java.net.http;
    requires java.sql;
    requires net.javacrumbs.shedlock.core;
    requires net.javacrumbs.shedlock.provider.jdbctemplate;
    requires net.javacrumbs.shedlock.spring;
    requires org.apache.logging.log4j;
    requires org.slf4j;
    requires org.springdoc.openapi.common;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.data.commons;
    requires spring.data.jdbc;
    requires spring.data.jpa;
    requires spring.data.rest.core;
    requires spring.data.rest.webmvc;
    requires spring.hateoas;
    requires spring.jdbc;
    requires spring.security.config;
    requires spring.security.core;
    requires spring.security.crypto;
    requires spring.security.oauth2.client;
    requires spring.security.oauth2.core;
    requires spring.security.oauth2.jose;
    requires spring.security.oauth2.resource.server;
    requires spring.security.web;
    requires spring.tx;
    requires spring.web;
    requires spring.webmvc;
    requires geoss.common;
}
