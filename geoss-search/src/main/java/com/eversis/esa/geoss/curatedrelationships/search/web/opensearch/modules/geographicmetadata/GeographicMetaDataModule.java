package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.model.Contact;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.model.OnlineResource;

import com.rometools.rome.feed.module.Module;

import java.util.List;

public interface GeographicMetaDataModule extends Module {

    List<OnlineResource> getOnlineResources();

    List<Contact> getContacts();

    List<BoundingBox> getBoundingBoxes();

    void setOnlineResources(final List<OnlineResource> onlineResources);

    void setContacts(final List<Contact> contacts);

    void setBoundingBox(final List<BoundingBox> boundingBoxes);

}
