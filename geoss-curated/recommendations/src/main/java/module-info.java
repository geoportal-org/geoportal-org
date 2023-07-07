module geoss.curated.recommendations {
    exports com.eversis.esa.geoss.curated.recommendations.domain;
    exports com.eversis.esa.geoss.curated.recommendations.repository;

    exports com.eversis.esa.geoss.curated.recommendations.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires spring.data.rest.core;
}
