package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Organisation;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.GeographicMetaDataModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.impl.GeographicMetaDataModuleImpl;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.model.Contact;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.model.OnlineResource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Geographic module factory.
 */
public class GeographicModuleFactory {

    private GeographicModuleFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create geographic meta data module geographic meta data module.
     *
     * @param transferOptions the transfer options
     * @param organisation the organisation
     * @param boundingBox the bounding box
     * @return the geographic meta data module
     */
    public static GeographicMetaDataModule createGeographicMetaDataModule(List<TransferOption> transferOptions,
            Organisation organisation,
            BoundingBox boundingBox) {
        GeographicMetaDataModule geographicMetaDataModule = new GeographicMetaDataModuleImpl();
        transferOptions = transferOptions != null ? transferOptions : Collections.emptyList();
        List<Organisation> organisations =
                organisation != null ? Collections.singletonList(organisation) : Collections.emptyList();
        List<BoundingBox> boundingBoxes =
                boundingBox != null ? Collections.singletonList(boundingBox) : Collections.emptyList();
        List<OnlineResource> onlineResources = transferOptions
                .stream()
                .map(OnlineResourceFactory::createOnlineResource)
                .collect(Collectors.toList());
        List<Contact> contacts = organisations.stream().map(ContactFactory::createContact).collect(Collectors.toList());
        geographicMetaDataModule.setOnlineResources(onlineResources);
        geographicMetaDataModule.setContacts(contacts);
        geographicMetaDataModule.setBoundingBox(boundingBoxes);
        return geographicMetaDataModule;
    }

}
