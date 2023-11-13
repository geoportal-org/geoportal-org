package com.eversis.esa.geoss.curated.resources.service.impl;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eversis.esa.geoss.curated.resources.domain.DashboardContents;
import com.eversis.esa.geoss.curated.resources.model.DashboardContentsModel;
import com.eversis.esa.geoss.curated.resources.repository.DashboardContentsRepository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The type Dashboard contents service impl test.
 */
@ContextConfiguration(classes = {DashboardContentsServiceImpl.class})
@ExtendWith(SpringExtension.class)
class DashboardContentsServiceImplTest {

    @MockBean
    private DashboardContentsRepository dashboardContentsRepository;

    @Autowired
    private DashboardContentsServiceImpl dashboardContentsServiceImpl;

    /**
     * Method under test: {@link DashboardContentsServiceImpl#getOrCreateDashboardContents(DashboardContentsModel)}
     */
    @Test
    void testGetOrCreateDashboardContents() {
        DashboardContents dashboardContents = new DashboardContents();
        dashboardContents.setContent("Not all who wander are lost");
        dashboardContents.setId(1L);
        Optional<DashboardContents> ofResult = Optional.of(dashboardContents);
        when(dashboardContentsRepository.findByContent(Mockito.<String>any())).thenReturn(ofResult);

        DashboardContentsModel dashboardContents2 = new DashboardContentsModel();
        dashboardContents2.setContent("Not all who wander are lost");
        DashboardContents actualOrCreateDashboardContents = dashboardContentsServiceImpl
                .getOrCreateDashboardContents(dashboardContents2);
        verify(dashboardContentsRepository).findByContent(Mockito.<String>any());
        assertSame(dashboardContents, actualOrCreateDashboardContents);
    }

    /**
     * Method under test: {@link DashboardContentsServiceImpl#getOrCreateDashboardContents(DashboardContentsModel)}
     */
    @Test
    void testGetOrCreateDashboardContents2() {
        DashboardContents dashboardContents = new DashboardContents();
        dashboardContents.setContent("Not all who wander are lost");
        dashboardContents.setId(1L);
        when(dashboardContentsRepository.save(Mockito.<DashboardContents>any())).thenReturn(dashboardContents);
        Optional<DashboardContents> emptyResult = Optional.empty();
        when(dashboardContentsRepository.findByContent(Mockito.<String>any())).thenReturn(emptyResult);

        DashboardContentsModel dashboardContents2 = new DashboardContentsModel();
        dashboardContents2.setContent("Not all who wander are lost");
        DashboardContents actualOrCreateDashboardContents = dashboardContentsServiceImpl
                .getOrCreateDashboardContents(dashboardContents2);
        verify(dashboardContentsRepository).findByContent(Mockito.<String>any());
        verify(dashboardContentsRepository).save(Mockito.<DashboardContents>any());
        assertSame(dashboardContents, actualOrCreateDashboardContents);
    }

    /**
     * Method under test: {@link DashboardContentsServiceImpl#getOrCreateDashboardContents(DashboardContentsModel)}
     */
    @Test
    void testGetOrCreateDashboardContents3() {
        DashboardContentsModel dashboardContents = mock(DashboardContentsModel.class);
        when(dashboardContents.getContent()).thenReturn("");
        doNothing().when(dashboardContents).setContent(Mockito.<String>any());
        dashboardContents.setContent("Not all who wander are lost");
        DashboardContents actualOrCreateDashboardContents = dashboardContentsServiceImpl
                .getOrCreateDashboardContents(dashboardContents);
        verify(dashboardContents, atLeast(1)).getContent();
        verify(dashboardContents).setContent(Mockito.<String>any());
        assertNull(actualOrCreateDashboardContents);
    }
}
