module geoss.curated.common {
    exports com.eversis.esa.geoss.curated.common.configuration to spring.beans, spring.boot, spring.context;
    exports com.eversis.esa.geoss.curated.common.email;
    exports com.eversis.esa.geoss.curated.common.domain;
    exports com.eversis.esa.geoss.curated.common.model;
    exports com.eversis.esa.geoss.curated.common.service;
    exports com.eversis.esa.geoss.curated.common.repository;

    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires io.swagger.v3.oas.annotations;
    requires jakarta.annotation;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.apache.logging.log4j;
    requires org.hibernate.orm.core;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context.support;
    requires spring.context;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.data.rest.core;
    requires spring.data.rest.webmvc;
    requires spring.security.core;
    requires spring.web;
    requires thymeleaf.spring6;
    requires thymeleaf;
    requires geoss.common;
}
