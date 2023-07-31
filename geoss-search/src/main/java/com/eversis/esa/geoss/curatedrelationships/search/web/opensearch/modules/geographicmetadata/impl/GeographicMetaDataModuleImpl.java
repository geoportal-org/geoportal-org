package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.GeographicMetaDataModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.model.Contact;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.model.OnlineResource;

import com.rometools.rome.feed.CopyFrom;
import com.rometools.rome.feed.module.ModuleImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class GeographicMetaDataModuleImpl extends ModuleImpl implements GeographicMetaDataModule {

    private transient List<OnlineResource> onlineResources;
    private transient List<Contact> contacts;
    private transient List<BoundingBox> boundingBoxes;

    public GeographicMetaDataModuleImpl() {
        super(GeographicMetaDataModule.class, GeographicMetaDataModuleConstants.GMD_URI);
    }

    @Override
    public List<OnlineResource> getOnlineResources() {
        return onlineResources != null ? onlineResources : Collections.emptyList();
    }

    @Override
    public void setOnlineResources(List<OnlineResource> onlineResources) {
        this.onlineResources = onlineResources;
    }

    @Override
    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public void setBoundingBox(List<BoundingBox> boundingBoxes) {
        this.boundingBoxes = boundingBoxes;
    }

    @Override
    public List<Contact> getContacts() {
        return this.contacts;
    }

    @Override
    public List<BoundingBox> getBoundingBoxes() {
        return this.boundingBoxes;
    }

    @Override
    public Class<? extends CopyFrom> getInterface() {
        return GeographicMetaDataModule.class;
    }

    @Override
    public void copyFrom(CopyFrom obj) {
        final GeographicMetaDataModule gmdModule = (GeographicMetaDataModule) obj;
        this.onlineResources = gmdModule.getOnlineResources()
                .stream()
                .map(onlineResource -> {
                    try {
                        return (OnlineResource) onlineResource.clone();
                    } catch (CloneNotSupportedException e) {
                        log.error("Could not clone object: {} : {}", onlineResource, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        this.boundingBoxes = gmdModule.getBoundingBoxes()
                .stream()
                .map(boundingBox -> new BoundingBox(boundingBox.getLeftTopPoint(), boundingBox.getRightBottomPoint()))
                .collect(Collectors.toList());
    }
}
