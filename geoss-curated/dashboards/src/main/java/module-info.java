module geoss.curated.dashboards {
    exports com.eversis.esa.geoss.curated.dashboards.configuration to spring.beans, spring.boot, spring.context;

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
    requires geoss.curated.resources;
    requires jakarta.validation;
    requires io.swagger.v3.oas.annotations;
    requires spring.data.rest.webmvc;
    requires spring.web;
    requires spring.tx;
    requires spring.security.core;
    requires spring.data.rest.core;
    requires org.apache.logging.log4j;
}
