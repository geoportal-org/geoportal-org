package com.eversis.esa.geoss.proxy.service.impl;

import com.eversis.esa.geoss.proxy.domain.ViewCounter;
import com.eversis.esa.geoss.proxy.domain.ViewCounterModel;
import com.eversis.esa.geoss.proxy.mapper.impl.ViewCounterMapper;
import com.eversis.esa.geoss.proxy.repository.ViewCounterRepository;
import com.eversis.esa.geoss.proxy.service.ViewCounterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ViewCounterServiceImpl implements ViewCounterService {

    private final ViewCounterRepository viewCounterRepository;

    private final ViewCounterMapper viewCounterMapper;

    /**
     * Instantiates a new Score service.
     *
     * @param viewCounterRepository the score repository
     * @param viewCounterMapper the score mapper
     */
    public ViewCounterServiceImpl(ViewCounterRepository viewCounterRepository, ViewCounterMapper viewCounterMapper) {
        this.viewCounterRepository = viewCounterRepository;
        this.viewCounterMapper = viewCounterMapper;
    }

    @Override
    public void increaseCounter(ViewCounterModel viewCounterModel) {
        viewCounterRepository.save(viewCounterMapper.mapToDocument(viewCounterModel));
    }

    @Override
    public ViewCounter getCounter(String entryId) {
        return null;
    }

}
