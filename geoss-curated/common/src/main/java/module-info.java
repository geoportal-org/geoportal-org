module geoss.curated.common {
    exports com.eversis.esa.geoss.curated.common.configuration to spring.beans, spring.boot, spring.context;
    exports com.eversis.esa.geoss.curated.common.email;

    requires static lombok;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires geoss.common;
    requires spring.context.support;
    requires thymeleaf;
    requires thymeleaf.spring6;
    requires org.apache.logging.log4j;

}
