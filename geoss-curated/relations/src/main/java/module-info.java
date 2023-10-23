module geoss.curated.relations {
    exports com.eversis.esa.geoss.curated.relations.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires jakarta.persistence;
    requires spring.data.jpa;
    requires org.hibernate.orm.core;
    requires com.fasterxml.jackson.annotation;
    requires geoss.curated.common;
    requires spring.data.commons;
    requires spring.data.rest.webmvc;
    requires spring.web;
    requires io.swagger.v3.oas.annotations;
    requires spring.security.core;
    requires jakarta.validation;
    requires spring.tx;
    requires org.apache.logging.log4j;
    requires jakarta.annotation;
}
