module geoss.settings.site {
    requires static lombok;
    requires geoss.common;
    requires geoss.instance.settings;
    requires geoss.system.settings;
    requires io.swagger.v3.oas.annotations;
    requires io.swagger.v3.oas.models;
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
