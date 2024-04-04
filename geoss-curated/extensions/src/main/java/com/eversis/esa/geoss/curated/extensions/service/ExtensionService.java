package com.eversis.esa.geoss.curated.extensions.service;

import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;

/**
 * The interface Extension service.
 */
public interface ExtensionService {

    EntryExtension findExtension(long extensionId);

}
