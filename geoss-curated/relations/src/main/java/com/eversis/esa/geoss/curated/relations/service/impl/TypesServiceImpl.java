package com.eversis.esa.geoss.curated.relations.service.impl;

import com.eversis.esa.geoss.curated.relations.domain.Types;
import com.eversis.esa.geoss.curated.relations.mapper.TypesMapper;
import com.eversis.esa.geoss.curated.relations.model.TypeModel;
import com.eversis.esa.geoss.curated.relations.repository.TypesRepository;
import com.eversis.esa.geoss.curated.relations.service.TypesService;
import org.springframework.stereotype.Service;

/**
 * The type Types service.
 */
@Service
public class TypesServiceImpl implements TypesService {

    private final TypesRepository typesRepository;

    /**
     * Instantiates a new Types service.
     *
     * @param typesRepository the types repository
     */
    public TypesServiceImpl(TypesRepository typesRepository) {
        this.typesRepository = typesRepository;
    }

    @Override
    public Types getOrCreateType(TypeModel type) {
        return typesRepository.findByCode(type.getCode())
                .orElseGet(() -> typesRepository.save(TypesMapper.mapType(type)));
    }

}
