package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Organisation;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.model.Contact;

public class ContactFactory {

    private ContactFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Contact createContact(Organisation organisation) {
        Contact contact = new Contact();
        contact.setEmail(organisation.getEmail());
        contact.setTitle(organisation.getTitle());
        contact.setContact(organisation.getContact());
        contact.setContactEmail(organisation.getContactEmail());
        return contact;
    }

}
