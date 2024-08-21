package com.eversis.esa.geoss.curated.extensions.service.impl;

import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.repository.EntryExtensionRepository;
import com.eversis.esa.geoss.curated.extensions.service.ExtensionService;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The type Extension service.
 */
@Service
public class ExtensionServiceImpl implements ExtensionService {

    private final EntryExtensionRepository entryExtensionRepository;

    /**
     * Instantiates a new Extension service.
     *
     * @param entryExtensionRepository the entry extension repository
     */
    public ExtensionServiceImpl(EntryExtensionRepository entryExtensionRepository) {
        this.entryExtensionRepository = entryExtensionRepository;
    }

    @Override
    public EntryExtension findExtension(long extensionId) {
        return entryExtensionRepository.findById(extensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Extension entity with id: " + extensionId + " does not exist"));
    }

}
