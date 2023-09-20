module geoss.curated.extensions {
    exports com.eversis.esa.geoss.curated.extensions.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.data.jpa;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires com.fasterxml.jackson.annotation;
    requires geoss.curated.common;
    requires spring.data.commons;
    requires jakarta.validation;
    requires io.swagger.v3.oas.annotations;
    requires spring.web;
    requires spring.data.rest.webmvc;
    requires spring.security.core;
    requires spring.tx;
    requires spring.data.rest.core;
    requires org.apache.logging.log4j;

}
