module geoss.curated.common {
    exports com.eversis.esa.geoss.curated.common.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
}
