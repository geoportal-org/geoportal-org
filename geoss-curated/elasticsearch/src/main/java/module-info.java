module geoss.curated.elasticsearch {
    exports com.eversis.esa.geoss.curated.elasticsearch.configuration to spring.beans, spring.boot, spring.context;
    exports com.eversis.esa.geoss.curated.elasticsearch.model;
    exports com.eversis.esa.geoss.curated.elasticsearch.service;
    exports com.eversis.esa.geoss.curated.elasticsearch.repository;

    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
    requires jakarta.annotation;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.apache.logging.log4j;
    requires org.springdoc.openapi.common;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires spring.data.commons;
    requires spring.data.rest.webmvc;
    requires spring.security.core;
    requires spring.tx;
    requires spring.web;
    requires geoss.curated.common;
    requires spring.data.rest.core;
    requires spring.data.elasticsearch;
    requires geoss.curated.resources;
    requires geoss.curated.relations;
    requires geoss.curated.extensions;
    requires geoss.curated.recommendations;

}
