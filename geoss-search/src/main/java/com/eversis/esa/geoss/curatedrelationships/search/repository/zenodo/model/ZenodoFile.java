package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.model;

import lombok.Data;

@Data
public class ZenodoFile {

    private String type;
    private String checksum;
    private String key;
    private ZenodoLinks links;

    public String getFileLink() {
        if (links != null) {
            return links.getSelf();
        }
        return "";
    }

}
