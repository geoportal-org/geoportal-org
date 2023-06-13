module geoss.common {
    exports com.eversis.esa.geoss.common.constraints;
    exports com.eversis.esa.geoss.common.domain;
    exports com.eversis.esa.geoss.common.hateoas;
    exports com.eversis.esa.geoss.common.properties;

    exports com.eversis.esa.geoss.common.configuration to spring.beans, spring.boot, spring.context;
    exports com.eversis.esa.geoss.common.event to spring.beans, spring.context;
    exports com.eversis.esa.geoss.common.exception to spring.beans;

    opens com.eversis.esa.geoss.common.exception;
    opens com.eversis.esa.geoss.common.hateoas to spring.core;

    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires jakarta.mail;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires org.apache.logging.log4j;
    requires org.hibernate.orm.envers;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.context.support;
    requires spring.core;
    requires spring.data.commons;
    requires spring.data.rest.core;
    requires spring.data.rest.webmvc;
    requires spring.hateoas;
    requires spring.tx;
    requires spring.web;
    requires spring.webmvc;
}
