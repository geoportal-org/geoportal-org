package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.DefinitionType;
import com.eversis.esa.geoss.curated.resources.model.DefinitionTypeModel;
import com.eversis.esa.geoss.curated.resources.repository.DefinitionTypeRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Definition type service impl test.
 */
@ContextConfiguration(classes = {DefinitionTypeServiceImpl.class})
@ExtendWith(SpringExtension.class)
class DefinitionTypeServiceImplTest {

    @MockBean
    private DefinitionTypeRepository definitionTypeRepository;

    @Autowired
    private DefinitionTypeServiceImpl definitionTypeServiceImpl;

    /**
     * Test get or create definition type.
     */
    @Test
    void testGetOrCreateDefinitionType() {
        DefinitionType definitionType = new DefinitionType();
        definitionType.setCode(0);
        definitionType.setId(1L);
        definitionType.setName("Predefined");
        Optional<DefinitionType> ofResult = Optional.of(definitionType);
        when(definitionTypeRepository.findByCode(anyInt())).thenReturn(ofResult);
        DefinitionType actualOrCreateDefinitionType = definitionTypeServiceImpl
                .getOrCreateDefinitionType(DefinitionTypeModel.PREDEFINED);
        verify(definitionTypeRepository).findByCode(anyInt());
        assertSame(definitionType, actualOrCreateDefinitionType);
    }

    /**
     * Test get or create definition type 2.
     */
    @Test
    void testGetOrCreateDefinitionType2() {
        DefinitionType definitionType = new DefinitionType();
        definitionType.setCode(0);
        definitionType.setId(1L);
        definitionType.setName("Predefined");
        when(definitionTypeRepository.save(Mockito.<DefinitionType>any())).thenReturn(definitionType);
        Optional<DefinitionType> emptyResult = Optional.empty();
        when(definitionTypeRepository.findByCode(anyInt())).thenReturn(emptyResult);
        DefinitionType actualOrCreateDefinitionType = definitionTypeServiceImpl
                .getOrCreateDefinitionType(DefinitionTypeModel.PREDEFINED);
        verify(definitionTypeRepository).findByCode(anyInt());
        verify(definitionTypeRepository).save(Mockito.<DefinitionType>any());
        assertSame(definitionType, actualOrCreateDefinitionType);
    }

    /**
     * Test get or create definition type 3.
     */
    @Test
    void testGetOrCreateDefinitionType3() {
        DefinitionType definitionType = new DefinitionType();
        definitionType.setCode(1);
        definitionType.setId(2L);
        definitionType.setName("User defined");
        Optional<DefinitionType> ofResult = Optional.of(definitionType);
        when(definitionTypeRepository.findByCode(anyInt())).thenReturn(ofResult);
        DefinitionType actualOrCreateDefinitionType = definitionTypeServiceImpl
                .getOrCreateDefinitionType(DefinitionTypeModel.USER_DEFINED);
        verify(definitionTypeRepository).findByCode(anyInt());
        assertSame(definitionType, actualOrCreateDefinitionType);
    }

    /**
     * Test get or create definition type 4.
     */
    @Test
    void testGetOrCreateDefinitionType4() {
        DefinitionType definitionType = new DefinitionType();
        definitionType.setCode(2);
        definitionType.setId(3L);
        definitionType.setName("External resource copy");
        Optional<DefinitionType> ofResult = Optional.of(definitionType);
        when(definitionTypeRepository.findByCode(anyInt())).thenReturn(ofResult);
        DefinitionType actualOrCreateDefinitionType = definitionTypeServiceImpl
                .getOrCreateDefinitionType(DefinitionTypeModel.EXTERNAL_RESOURCE_COPY);
        verify(definitionTypeRepository).findByCode(anyInt());
        assertSame(definitionType, actualOrCreateDefinitionType);
    }
}
