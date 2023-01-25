module geoss.settings.common {
    exports com.eversis.esa.geoss.settings.common.constraints;
    exports com.eversis.esa.geoss.settings.common.hateoas;

    exports com.eversis.esa.geoss.settings.common.configuration to spring.beans, spring.boot, spring.context;
    exports com.eversis.esa.geoss.settings.common.event to spring.beans, spring.context;

    opens com.eversis.esa.geoss.settings.common.hateoas to spring.core;

    requires com.fasterxml.jackson.annotation;
    requires jakarta.mail;
    requires jakarta.validation;
    requires lombok;
    requires org.apache.logging.log4j;
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
