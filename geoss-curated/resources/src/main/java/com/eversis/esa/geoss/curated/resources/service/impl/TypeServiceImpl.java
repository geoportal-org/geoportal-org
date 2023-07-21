package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.Type;
import com.eversis.esa.geoss.curated.resources.mapper.TypeMapper;
import com.eversis.esa.geoss.curated.resources.model.TypeModel;
import com.eversis.esa.geoss.curated.resources.repository.TypeRepository;
import com.eversis.esa.geoss.curated.resources.service.TypeService;
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
