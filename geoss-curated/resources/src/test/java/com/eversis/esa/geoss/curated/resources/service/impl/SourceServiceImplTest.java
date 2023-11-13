package com.eversis.esa.geoss.curated.resources.service.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eversis.esa.geoss.curated.resources.domain.Source;
import com.eversis.esa.geoss.curated.resources.model.SourceModel;
import com.eversis.esa.geoss.curated.resources.repository.SourceRepository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The type Source service impl test.
 */
@ContextConfiguration(classes = {SourceServiceImpl.class})
@ExtendWith(SpringExtension.class)
class SourceServiceImplTest {

    @MockBean
    private SourceRepository sourceRepository;

    @Autowired
    private SourceServiceImpl sourceServiceImpl;

    /**
     * Method under test: {@link SourceServiceImpl#getOrCreateSource(SourceModel)}
     */
    @Test
    void testGetOrCreateSource() {
        Source source = new Source();
        source.setCode("wikipedia");
        source.setId(5L);
        source.setIsCustom(0);
        source.setTerm("Wikipedia");
        Optional<Source> ofResult = Optional.of(source);
        when(sourceRepository.findByCode(Mockito.<String>any())).thenReturn(ofResult);

        SourceModel source2 = new SourceModel();
        source2.setCode("wikipedia");
        source2.setTerm("Wikipedia");
        Source actualOrCreateSource = sourceServiceImpl.getOrCreateSource(source2);
        verify(sourceRepository).findByCode(Mockito.<String>any());
        assertSame(source, actualOrCreateSource);
    }

    /**
     * Method under test: {@link SourceServiceImpl#getOrCreateSource(SourceModel)}
     */
    @Test
    void testGetOrCreateSource2() {
        Source source = new Source();
        source.setCode("wikipedia");
        source.setId(5L);
        source.setIsCustom(0);
        source.setTerm("Wikipedia");
        when(sourceRepository.save(Mockito.<Source>any())).thenReturn(source);
        Optional<Source> emptyResult = Optional.empty();
        when(sourceRepository.findByCode(Mockito.<String>any())).thenReturn(emptyResult);

        SourceModel source2 = new SourceModel();
        source2.setCode("wikipedia");
        source2.setTerm("Wikipedia");
        Source actualOrCreateSource = sourceServiceImpl.getOrCreateSource(source2);
        verify(sourceRepository).findByCode(Mockito.<String>any());
        verify(sourceRepository).save(Mockito.<Source>any());
        assertSame(source, actualOrCreateSource);
    }
}
