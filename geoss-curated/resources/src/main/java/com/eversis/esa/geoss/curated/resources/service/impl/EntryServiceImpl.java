package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.mapper.EntryMapper;
import com.eversis.esa.geoss.curated.resources.model.EntryModel;
import com.eversis.esa.geoss.curated.resources.repository.EntryRepository;
import com.eversis.esa.geoss.curated.resources.service.EntryService;

import org.springframework.stereotype.Service;

/**
 * The type Entry service.
 */
@Service
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;

    private final EntryMapper entryMapper;

    /**
     * Instantiates a new Entry service.
     *
     * @param entryRepository the entry repository
     */
    public EntryServiceImpl(EntryRepository entryRepository, EntryMapper entryMapper) {
        this.entryRepository = entryRepository;
        this.entryMapper = entryMapper;
    }

    @Override
    public Entry getOrCreateEntry(EntryModel entry) {
        return entryRepository.findByCode(entry.getCode())
                .orElseGet(() -> entryRepository.save(entryMapper.mapToEntry(entry)));
    }

}
