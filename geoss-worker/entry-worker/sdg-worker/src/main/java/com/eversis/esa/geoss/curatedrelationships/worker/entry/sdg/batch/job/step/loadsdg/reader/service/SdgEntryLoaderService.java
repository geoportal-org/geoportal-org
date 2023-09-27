package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config.sdg.SdgProperties;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Sdg entry loader service.
 */
@Log4j2
@Service
public class SdgEntryLoaderService {

    private final UNEntryLoaderService unEntryLoaderService;

    private final GeoPortalEntryLoaderService geoPortalEntryLoaderService;

    private final SdgProperties sdgProperties;

    /**
     * Instantiates a new Sdg entry loader service.
     *
     * @param unEntryLoaderService the un entry loader service
     * @param geoPortalEntryLoaderService the geo portal entry loader service
     */
    public SdgEntryLoaderService(
            UNEntryLoaderService unEntryLoaderService,
            GeoPortalEntryLoaderService geoPortalEntryLoaderService,
            SdgProperties sdgProperties) {
        this.unEntryLoaderService = unEntryLoaderService;
        this.geoPortalEntryLoaderService = geoPortalEntryLoaderService;
        this.sdgProperties = sdgProperties;
    }

    /**
     * Fetch entries list.
     *
     * @return the list
     */
    public List<Entry> fetchEntries() {
        List<Entry> entries = unEntryLoaderService.fetchEntries();
        if (sdgProperties.isFetchMissingEntries()) {
            List<Entry> missingEntries = fetchMissingEntries(
                    entries.stream().map(Entry::getCode).collect(Collectors.toList()));
            log.info("Adding {} missing entries to UN entries from GeoPortal", missingEntries.size());
            entries.addAll(missingEntries);
        }
        log.info("Fetched {} entries in total", entries.size());
        return entries;
    }

    private List<Entry> fetchMissingEntries(@NonNull List<String> alreadyDownloadedEntryCodes) {
        List<Entry> geoPortalEntries = geoPortalEntryLoaderService.fetchEntries();
        return getMissingSdgEntries(geoPortalEntries, alreadyDownloadedEntryCodes);
    }

    private List<Entry> getMissingSdgEntries(@NonNull List<Entry> newEntries,
            @NonNull List<String> alreadyDownloadedEntryCodes) {
        return newEntries.stream()
                .filter(entry -> !alreadyDownloadedEntryCodes.contains(entry.getCode()))
                .collect(Collectors.toList());
    }

}
