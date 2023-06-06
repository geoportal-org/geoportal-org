module geoss.personaldata.common {
    exports com.eversis.esa.geoss.personaldata.common.constraints;
    exports com.eversis.esa.geoss.personaldata.common.domain;
    exports com.eversis.esa.geoss.personaldata.common.hateoas;
    exports com.eversis.esa.geoss.personaldata.common.properties;

    exports com.eversis.esa.geoss.personaldata.common.configuration to spring.beans, spring.boot, spring.context;
    exports com.eversis.esa.geoss.personaldata.common.event to spring.beans, spring.context;

    opens com.eversis.esa.geoss.personaldata.common.hateoas to spring.core;

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
    requires spring.data.commons;
    requires spring.hateoas;
    requires spring.tx;
    requires spring.web;
    requires spring.webmvc;
}
