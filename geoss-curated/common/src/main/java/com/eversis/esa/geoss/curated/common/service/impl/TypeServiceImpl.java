package com.eversis.esa.geoss.curated.common.service.impl;

import com.eversis.esa.geoss.curated.common.domain.Type;
import com.eversis.esa.geoss.curated.common.mapper.TypeMapper;
import com.eversis.esa.geoss.curated.common.model.TypeModel;
import com.eversis.esa.geoss.curated.common.repository.TypeRepository;
import com.eversis.esa.geoss.curated.common.service.TypeService;

import org.springframework.stereotype.Service;

/**
 * The type Type service.
 */
@Service
public class TypeServiceImpl implements TypeService {

    private final TypeRepository typeRepository;

    /**
     * Instantiates a new Type service.
     *
     * @param typeRepository the type repository
     */
    public TypeServiceImpl(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Override
    public Type getOrCreateType(TypeModel type) {
        return typeRepository.findByCode(type.getCode())
                .orElseGet(() -> typeRepository.save(TypeMapper.mapType(type)));
    }

}
