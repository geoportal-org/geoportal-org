module geoss.settings.dab {
    exports com.eversis.esa.geoss.settings.dab.properties;

    exports com.eversis.esa.geoss.settings.dab.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires com.rometools.rome;
    requires com.rometools.rome.modules;
    requires geoss.common;
    requires geoss.settings.common;
    requires geoss.instance.settings;
    requires geoss.system.settings;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
    requires java.net.http;
    requires net.javacrumbs.shedlock.spring;
    requires org.apache.logging.log4j;
    requires org.springdoc.openapi.common;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.data.commons;
    requires spring.data.rest.webmvc;
    requires spring.hateoas;
    requires spring.security.core;
    requires spring.tx;
    requires spring.web;
    requires spring.webmvc;
}
