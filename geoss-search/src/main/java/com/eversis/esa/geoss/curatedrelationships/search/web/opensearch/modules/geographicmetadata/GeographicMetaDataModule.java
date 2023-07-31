package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.model.Contact;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.model.OnlineResource;

import com.rometools.rome.feed.module.Module;

import java.util.List;

/**
 * The interface Geographic meta data module.
 */
public interface GeographicMetaDataModule extends Module {

    /**
     * Gets online resources.
     *
     * @return the online resources
     */
    List<OnlineResource> getOnlineResources();

    /**
     * Gets contacts.
     *
     * @return the contacts
     */
    List<Contact> getContacts();

    /**
     * Gets bounding boxes.
     *
     * @return the bounding boxes
     */
    List<BoundingBox> getBoundingBoxes();

    /**
     * Sets online resources.
     *
     * @param onlineResources the online resources
     */
    void setOnlineResources(final List<OnlineResource> onlineResources);

    /**
     * Sets contacts.
     *
     * @param contacts the contacts
     */
    void setContacts(final List<Contact> contacts);

    /**
     * Sets bounding box.
     *
     * @param boundingBoxes the bounding boxes
     */
    void setBoundingBox(final List<BoundingBox> boundingBoxes);

}
