package com.eversis.esa.geoss.curated.extensions.service;

import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;

/**
 * The interface Extension service.
 */
public interface ExtensionService {

    /**
     * Find extension entry extension.
     *
     * @param extensionId the extension id
     * @return the entry extension
     */
    EntryExtension findExtension(long extensionId);

}
