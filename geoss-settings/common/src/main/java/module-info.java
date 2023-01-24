module geoss.settings.common {
    exports com.eversis.esa.geoss.settings.common.configuration;
    exports com.eversis.esa.geoss.settings.common.constraints;
    exports com.eversis.esa.geoss.settings.common.hateoas;

    opens com.eversis.esa.geoss.settings.common.hateoas to spring.core;

    requires com.fasterxml.jackson.annotation;
    requires jakarta.validation;
    requires lombok;
    requires org.apache.logging.log4j;
    requires spring.beans;
    requires spring.boot;
    requires spring.context;
    requires spring.data.commons;
    requires spring.hateoas;
    requires spring.web;
    requires spring.webmvc;
}
