module geoss.personaldata.run {
    exports com.eversis.esa.geoss.personaldata.run.domain;
    exports com.eversis.esa.geoss.personaldata.run.repository;

    exports com.eversis.esa.geoss.personaldata.run.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires geoss.common;
    requires com.fasterxml.jackson.annotation;
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
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.data.rest.core;
    requires spring.hateoas;
    requires spring.security.core;
    requires spring.tx;
    requires spring.web;
    requires spring.webmvc;
}
