package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.config;

public class IndexConfigurationProperties {

    private String index;
    private String type;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
