module geoss.curated.recommendations {
    exports com.eversis.esa.geoss.curated.recommendations.domain;
    exports com.eversis.esa.geoss.curated.recommendations.model;
    exports com.eversis.esa.geoss.curated.recommendations.repository;
    exports com.eversis.esa.geoss.curated.recommendations.service;

    exports com.eversis.esa.geoss.curated.recommendations.configuration to spring.beans, spring.boot, spring.context;
    exports com.eversis.esa.geoss.curated.recommendations.support;

    requires static lombok;
    requires static org.mapstruct;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.apache.logging.log4j;
    requires org.hibernate.orm.core;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.core;
    requires spring.context;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.data.rest.core;
    requires spring.data.rest.webmvc;
    requires spring.tx;
}
