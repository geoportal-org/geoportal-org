package com.eversis.esa.geoss.proxy.service.impl;

import com.eversis.esa.geoss.proxy.domain.Score;
import com.eversis.esa.geoss.proxy.domain.ScoreModel;
import com.eversis.esa.geoss.proxy.mapper.impl.ScoreMapper;
import com.eversis.esa.geoss.proxy.repository.ScoreRepository;
import com.eversis.esa.geoss.proxy.service.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The type Score service.
 */
@Slf4j
@Service
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;

    private final ScoreMapper scoreMapper;

    /**
     * Instantiates a new Score service.
     *
     * @param scoreRepository the score repository
     * @param scoreMapper the score mapper
     */
    public ScoreServiceImpl(ScoreRepository scoreRepository, ScoreMapper scoreMapper) {
        this.scoreRepository = scoreRepository;
        this.scoreMapper = scoreMapper;
    }

    @Override
    public void addScore(ScoreModel scoreModel) {
        scoreRepository.save(scoreMapper.mapToDocument(scoreModel));
    }

    @Override
    public Score getScore(String entryId) {
        return null;
    }

}
