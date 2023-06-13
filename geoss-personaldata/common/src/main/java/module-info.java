module geoss.personaldata.common {
    exports com.eversis.esa.geoss.personaldata.common.configuration to spring.beans, spring.boot, spring.context;

    requires static lombok;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
}
