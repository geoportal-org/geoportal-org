module geoss.personaldata.feedback {
    exports com.eversis.esa.geoss.personaldata.feedback.domain;
    exports com.eversis.esa.geoss.personaldata.feedback.repository;
    exports com.eversis.esa.geoss.personaldata.feedback.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
    requires jakarta.annotation;
    requires jakarta.mail;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.apache.logging.log4j;
    requires org.hibernate.orm.core;
    requires org.hibernate.orm.envers;
    requires org.springdoc.openapi.common;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.core;
    requires spring.context;
    requires spring.context.support;
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
    requires geoss.personaldata.common;
}
