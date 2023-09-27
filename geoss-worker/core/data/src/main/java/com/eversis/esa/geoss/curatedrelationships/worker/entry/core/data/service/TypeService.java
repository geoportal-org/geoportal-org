package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Type;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository.TypeRepository;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper.TypeMapper;

import org.springframework.stereotype.Service;

/**
 * The type Type service.
 */
@Service
public class TypeService {

    private final TypeRepository typeRepository;

    /**
     * Instantiates a new Type service.
     *
     * @param typeRepository the type repository
     */
    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    /**
     * Gets or create type.
     *
     * @param type the type
     * @return the or create type
     */
    public Type getOrCreateType(com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Type type) {
        return typeRepository.findByCode(type.getCode())
                .orElseGet(() -> typeRepository.save(TypeMapper.mapType(type)));
    }

}
