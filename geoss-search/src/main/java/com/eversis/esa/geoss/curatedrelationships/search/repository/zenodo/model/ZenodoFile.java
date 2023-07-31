package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.model;

import lombok.Data;

/**
 * The type Zenodo file.
 */
@Data
public class ZenodoFile {

    private String type;
    private String checksum;
    private String key;
    private ZenodoLinks links;

    /**
     * Gets file link.
     *
     * @return the file link
     */
    public String getFileLink() {
        if (links != null) {
            return links.getSelf();
        }
        return "";
    }

}
