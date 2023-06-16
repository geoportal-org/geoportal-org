package com.eversis.esa.geoss.proxy.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.eversis.esa.geoss.proxy.domain.GetMostPopularModel;
import com.eversis.esa.geoss.proxy.domain.GetNumberOfSearchesModel;
import com.eversis.esa.geoss.proxy.domain.MostPopular;
import com.eversis.esa.geoss.proxy.domain.Search;
import com.eversis.esa.geoss.proxy.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The type Statistics service.
 */
@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Override
    public List<Search> getNumberOfSearches(GetNumberOfSearchesModel getNumberOfSearchesModel) {
        return new ArrayList<>();
    }

    @Override
    public List<MostPopular> getMostPopularResources(GetMostPopularModel getMostPopularModel) {
        return new ArrayList<>();
    }

    @Override
    public List<MostPopular> getMostPopularKeywords(GetMostPopularModel getMostPopularModel) {
        return new ArrayList<>();
    }

    @Override
    public List<MostPopular> getMostPopularCatalogs(GetMostPopularModel getMostPopularModel) {
        return new ArrayList<>();
    }

    @Override
    public List<MostPopular> getMostPopularOrganizations(GetMostPopularModel getMostPopularModel) {
        return new ArrayList<>();
    }

    @Override
    public List<MostPopular> getMostPopularAreas(GetMostPopularModel getMostPopularModel) {
        return new ArrayList<>();
    }

}
