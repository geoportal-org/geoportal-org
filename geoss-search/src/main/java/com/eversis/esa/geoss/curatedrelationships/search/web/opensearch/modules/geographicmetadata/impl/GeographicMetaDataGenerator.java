package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;
import com.eversis.esa.geoss.curatedrelationships.search.utils.StringUtils;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper.ElementFactory;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.GeographicMetaDataModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.model.Contact;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.model.OnlineResource;

import com.rometools.rome.feed.module.Module;
import com.rometools.rome.io.ModuleGenerator;
import lombok.NonNull;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.impl.GeographicMetaDataResponseConstants.CHARACTER_STRING;
import static com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.impl.GeographicMetaDataResponseConstants.DECIMAL;

public class GeographicMetaDataGenerator implements ModuleGenerator {

    private static final Namespace GMD_NAMESPACE = Namespace.getNamespace("gmd", GeographicMetaDataModuleConstants.GMD_URI);
    private static final Namespace GCO_NAMESPACE = Namespace.getNamespace("gco", GeographicMetaDataModuleConstants.GCO_URI);
    private static final Namespace GMX_NAMESPACE = Namespace.getNamespace("gmx", GeographicMetaDataModuleConstants.GMX_URI);
    private static final Namespace XLINK_NAMESPACE = Namespace.getNamespace("xlink", GeographicMetaDataModuleConstants.XLINK_URI);

    @Override
    public String getNamespaceUri() {
        return GeographicMetaDataModuleConstants.GMD_URI;
    }

    @Override
    public Set<Namespace> getNamespaces() {
        return Collections.singleton(GMD_NAMESPACE);
    }

    @Override
    public void generate(final Module module, final Element element) {
        GeographicMetaDataModule geographicMetaDataModule = (GeographicMetaDataModule) module;

        List<OnlineResource> onlineResources = geographicMetaDataModule.getOnlineResources();
        List<Contact> contacts = geographicMetaDataModule.getContacts();
        List<BoundingBox> boundingBoxes = geographicMetaDataModule.getBoundingBoxes();

        if (!CollectionUtils.isEmpty(boundingBoxes)) {
            Element identificationInfoElement = generateIdentificationInfoElement(boundingBoxes);
            element.addContent(identificationInfoElement);
        }

        if (!CollectionUtils.isEmpty(contacts)) {
            Element contactElement = generateContactElement(contacts);
            element.addContent(contactElement);
        }

        Element distributionInfoElement = generateDistributionInfoElement(onlineResources);
        element.addContent(distributionInfoElement);
    }

    protected Element generateContactElement(@NonNull List<Contact> contacts) {
        Element contactElement = new Element("contact", GMD_NAMESPACE);
        contacts.forEach(contact -> contactElement.addContent(generateResponsiblePartyElement(contact)));
        return contactElement;
    }

    protected Element generateResponsiblePartyElement(Contact contact) {
        Element ciResponsiblePartyElement = new Element("CI_ResponsibleParty", GMD_NAMESPACE);
        if (StringUtils.isNotBlank(contact.getContact())) {
            Element individualNameElement = new Element("individualName", GMD_NAMESPACE);
            individualNameElement.addContent(ElementFactory.createElement(CHARACTER_STRING, contact.getContact(), GCO_NAMESPACE));
            ciResponsiblePartyElement.addContent(individualNameElement);
        }
        if (StringUtils.isNotBlank(contact.getTitle())) {
            Element organisationNameElement = new Element("organisationName", GMD_NAMESPACE);
            organisationNameElement.addContent(ElementFactory.createElement(CHARACTER_STRING, contact.getTitle(), GCO_NAMESPACE));
            ciResponsiblePartyElement.addContent(organisationNameElement);
        }
        if (StringUtils.isNotBlank(contact.getContactEmail())) {
            Element contactInfoElement = new Element("contactInfo", GMD_NAMESPACE);
            Element ciContactElement = new Element("CI_Contact", GMD_NAMESPACE);
            Element addressElement = new Element("address", GMD_NAMESPACE);
            Element ciAddressElement = new Element("CI_Address", GMD_NAMESPACE);
            Element emailAddressElement = new Element("electronicMailAddress", GMD_NAMESPACE);

            emailAddressElement.addContent(ElementFactory.createElement(CHARACTER_STRING, contact.getContactEmail(), GCO_NAMESPACE));
            ciAddressElement.addContent(emailAddressElement);
            addressElement.addContent(ciAddressElement);
            ciContactElement.addContent(addressElement);
            contactInfoElement.addContent(ciContactElement);
            ciResponsiblePartyElement.addContent(contactInfoElement);
        }

        return ciResponsiblePartyElement;
    }

    protected Element generateIdentificationInfoElement(@NonNull List<BoundingBox> boundingBoxes) {
        Element identificationInfoElement = new Element("identificationInfo", GMD_NAMESPACE);
        identificationInfoElement.addNamespaceDeclaration(GMD_NAMESPACE);
        identificationInfoElement.addNamespaceDeclaration(GMX_NAMESPACE);
        identificationInfoElement.addNamespaceDeclaration(GCO_NAMESPACE);
        identificationInfoElement.addNamespaceDeclaration(XLINK_NAMESPACE);
        Element mdDataIdentificationElement = new Element("MD_DataIdentification", GMD_NAMESPACE);
        Element extentElement = new Element("extent", GMD_NAMESPACE);
        Element exExtentElement = new Element("EX_Extent", GMD_NAMESPACE);
        Element geographicElement = new Element("geographicElement", GMD_NAMESPACE);

        boundingBoxes.forEach(boundingBox -> geographicElement.addContent(generateGeographicBoundingBoxElement(boundingBox)));
        exExtentElement.addContent(geographicElement);
        extentElement.addContent(exExtentElement);
        mdDataIdentificationElement.addContent(extentElement);
        identificationInfoElement.addContent(mdDataIdentificationElement);
        return identificationInfoElement;
    }

    protected Element generateGeographicBoundingBoxElement(BoundingBox boundingBox) {
        Element exGeographicBoundingBoxElement = new Element("EX_GeographicBoundingBox", GMD_NAMESPACE);
        Element eastBoundLongitudeElement = new Element("eastBoundLongitude", GMD_NAMESPACE);
        Element northBoundLatitudeElement = new Element("northBoundLatitude", GMD_NAMESPACE);
        Element southBoundLatitudeElement = new Element("southBoundLatitude", GMD_NAMESPACE);
        Element westBoundLongitudeElement = new Element("westBoundLongitude", GMD_NAMESPACE);

        eastBoundLongitudeElement.addContent(ElementFactory.createElement(DECIMAL,
                String.valueOf(boundingBox.getRightBottomPoint().getLongitude()), GCO_NAMESPACE));
        northBoundLatitudeElement.addContent(ElementFactory.createElement(DECIMAL,
                String.valueOf(boundingBox.getLeftTopPoint().getLatitude()), GCO_NAMESPACE));
        southBoundLatitudeElement.addContent(ElementFactory.createElement(DECIMAL,
                String.valueOf(boundingBox.getRightBottomPoint().getLatitude()), GCO_NAMESPACE));
        westBoundLongitudeElement.addContent(ElementFactory.createElement(DECIMAL,
                String.valueOf(boundingBox.getLeftTopPoint().getLongitude()), GCO_NAMESPACE));

        exGeographicBoundingBoxElement.addContent(eastBoundLongitudeElement);
        exGeographicBoundingBoxElement.addContent(northBoundLatitudeElement);
        exGeographicBoundingBoxElement.addContent(southBoundLatitudeElement);
        exGeographicBoundingBoxElement.addContent(westBoundLongitudeElement);
        return exGeographicBoundingBoxElement;
    }

    protected Element generateDistributionInfoElement(List<OnlineResource> onlineResources) {
        Element distributionInfoElement = new Element("distributionInfo", GMD_NAMESPACE);
        distributionInfoElement.addNamespaceDeclaration(GMD_NAMESPACE);
        distributionInfoElement.addNamespaceDeclaration(GMX_NAMESPACE);
        distributionInfoElement.addNamespaceDeclaration(GCO_NAMESPACE);
        distributionInfoElement.addNamespaceDeclaration(XLINK_NAMESPACE);

        Element mdDistributionElement = new Element("MD_Distribution", GMD_NAMESPACE);
        Element transferOptionsElement = new Element("transferOptions", GMD_NAMESPACE);
        Element digitalTransferOptionsElement = new Element("MD_DigitalTransferOptions", GMD_NAMESPACE);

        transferOptionsElement.addContent(digitalTransferOptionsElement);
        mdDistributionElement.addContent(transferOptionsElement);
        distributionInfoElement.addContent(mdDistributionElement);

        if (!CollectionUtils.isEmpty(onlineResources)) {
            onlineResources.forEach(onlineResource -> {
                Element onlineElement = new Element("onLine", GMD_NAMESPACE);
                onlineElement.addContent(generateOnlineResourceElement(onlineResource));
                digitalTransferOptionsElement.addContent(onlineElement);
            });
        }

        return distributionInfoElement;
    }

    protected Element generateOnlineResourceElement(OnlineResource onlineResource) {
        Element onlineResourceElement = new Element("CI_OnlineResource", GMD_NAMESPACE);

        if (StringUtils.isNotBlank(onlineResource.getUrl())) {
            Element linkageElement = new Element("linkage", GMD_NAMESPACE);
            linkageElement.addContent(ElementFactory.createElement("URL", onlineResource.getUrl(), GMD_NAMESPACE));
            onlineResourceElement.addContent(linkageElement);
        }
        if (StringUtils.isNotBlank(onlineResource.getProtocol())) {
            Element protocolElement = new Element("protocol", GMD_NAMESPACE);
            protocolElement.addContent(ElementFactory.createElement(CHARACTER_STRING, onlineResource.getProtocol(), GCO_NAMESPACE));
            onlineResourceElement.addContent(protocolElement);
        }
        if (StringUtils.isNotBlank(onlineResource.getName())) {
            Element nameElement = new Element("name", GMD_NAMESPACE);
            nameElement.addContent(ElementFactory.createElement(CHARACTER_STRING, onlineResource.getName(), GCO_NAMESPACE));
            onlineResourceElement.addContent(nameElement);
        }
        if (StringUtils.isNotBlank(onlineResource.getDescription())) {
            Element descriptionElement = new Element("description", GMD_NAMESPACE);
            descriptionElement.addContent(ElementFactory.createElement(CHARACTER_STRING, onlineResource.getDescription(), GCO_NAMESPACE));

            if (StringUtils.isNotBlank(onlineResource.getDescriptionURL())) {
                Element anchorLinkElement = new Element("Anchor", GMX_NAMESPACE);
                anchorLinkElement.setAttribute("href", onlineResource.getDescriptionURL(), XLINK_NAMESPACE);
                descriptionElement.addContent(anchorLinkElement);
            }

            onlineResourceElement.addContent(descriptionElement);
        }
        return onlineResourceElement;
    }
}
