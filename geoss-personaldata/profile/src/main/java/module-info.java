module geoss.personaldata.profile {
    exports com.eversis.esa.geoss.personaldata.profile.domain;
    exports com.eversis.esa.geoss.personaldata.profile.repository;

    exports com.eversis.esa.geoss.personaldata.profile.configuration to spring.beans, spring.boot, spring.context;

    requires geoss.personaldata.common;
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
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.data.rest.core;
    requires spring.hateoas;
    requires spring.tx;
    requires spring.web;
    requires spring.webmvc;
}
