module geoss.curated.common {
    exports com.eversis.esa.geoss.curated.common.configuration to spring.beans, spring.boot, spring.context;
    exports com.eversis.esa.geoss.curated.common.email;
    exports com.eversis.esa.geoss.curated.common.domain;

    requires static lombok;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires geoss.common;
    requires spring.context.support;
    requires thymeleaf;
    requires thymeleaf.spring6;
    requires org.apache.logging.log4j;
    requires com.fasterxml.jackson.annotation;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires spring.data.rest.core;
    requires spring.data.jpa;
    requires spring.data.rest.webmvc;
    requires spring.web;
    requires io.swagger.v3.oas.annotations;
    requires spring.security.core;
    requires spring.data.commons;
    requires jakarta.validation;

}
