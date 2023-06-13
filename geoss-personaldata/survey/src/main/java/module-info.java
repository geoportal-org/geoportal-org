module geoss.personaldata.survey {
    exports com.eversis.esa.geoss.personaldata.survey.domain;
    exports com.eversis.esa.geoss.personaldata.survey.repository;

    exports com.eversis.esa.geoss.personaldata.survey.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires geoss.common;
    requires com.fasterxml.jackson.annotation;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
    requires jakarta.persistence;
    requires jakarta.validation;
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
    requires spring.data.rest.webmvc;
    requires spring.hateoas;
    requires spring.tx;
    requires spring.web;
    requires spring.webmvc;
}
