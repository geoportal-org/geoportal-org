package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.EntryRating;
import com.eversis.esa.geoss.curated.resources.domain.EntryStats;
import com.eversis.esa.geoss.curated.resources.mapper.EntryRatingMapper;
import com.eversis.esa.geoss.curated.resources.model.CommentResponse;
import com.eversis.esa.geoss.curated.resources.model.EntryRatingModel;
import com.eversis.esa.geoss.curated.resources.model.EntryRatingWithCommentModel;
import com.eversis.esa.geoss.curated.resources.model.EntryRatingWithoutCommentModel;
import com.eversis.esa.geoss.curated.resources.model.RateResponse;
import com.eversis.esa.geoss.curated.resources.model.StatsResponse;
import com.eversis.esa.geoss.curated.resources.repository.EntryRatingRepository;
import com.eversis.esa.geoss.curated.resources.service.RatingService;
import com.eversis.esa.geoss.curated.resources.service.StatsService;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Rating service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
public class RatingServiceImpl implements RatingService {

    private static final double MIN_SCORE = 1;

    private static final double MAX_SCORE = 5;

    private static final int MAX_COMMENT = 4000;

    private final EntryRatingRepository entryRatingRepository;

    private final EntryRatingMapper entryRatingMapper;

    private final StatsService statsService;

    /**
     * Instantiates a new Rating service.
     *
     * @param entryRatingRepository the entry rating repository
     * @param entryRatingMapper the entry rating mapper
     * @param statsService the stats service
     */
    public RatingServiceImpl(EntryRatingRepository entryRatingRepository, EntryRatingMapper entryRatingMapper,
            StatsService statsService) {
        this.entryRatingRepository = entryRatingRepository;
        this.entryRatingMapper = entryRatingMapper;
        this.statsService = statsService;
    }

    @Override
    public List<EntryRating> findRatings() {
        log.info("Finding ratings");
        return entryRatingRepository.findAll();
    }

    @Override
    public EntryRating findRating(long entryId) {
        log.info("Finding rating with entryId: {}", entryId);
        return entryRatingRepository.findById(entryId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Entry rating with entryId: " + entryId + " does not exist"));
    }

    @Transactional
    @Override
    public void deleteRating(long entryId) {
        log.info("Deleting rating with entryId: {}", entryId);
        entryRatingRepository.deleteById(entryId);
    }

    @Transactional
    @Override
    public void createRating(EntryRatingModel entryRatingDto) {
        log.info("Creating new entry rating - {}", entryRatingDto);
        EntryRating entryRating = entryRatingRepository.save(entryRatingMapper.mapToEntryRating(entryRatingDto));
        log.info("Created new entry rating with entryId: {}", entryRating.getEntryId());
    }

    @Transactional
    @Override
    public void updateRating(long entryId, EntryRatingModel entryRatingDto) {
        log.info("Updating entry rating with entryId {}, using model {}", entryId, entryRatingDto);
        final EntryRating entryRating = entryRatingRepository.findById(entryId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Entry rating entity with entryId: " + entryId + " does not exist"));
        entryRatingRepository.save(entryRatingMapper.mapToEntryRating(entryRatingDto, entryRating));
        log.info("Updated entry rating with entryId: {}", entryRating.getEntryId());
    }

    @Transactional
    @Override
    public RateResponse rateWithComment(EntryRatingWithCommentModel entryRatingDto) {
        log.info("Rating with comment using model {}", entryRatingDto);
        entryRatingDto = validateEntryRatingDto(entryRatingDto);
        RateResponse rateResponse = new RateResponse();
        rateResponse.setTargetId(entryRatingDto.getTargetId());
        EntryRating entryRating = entryRatingRepository.save(entryRatingMapper.mapToEntryRating(entryRatingDto));
        rateResponse.setScore(entryRating.getScore());
        rateResponse.setComment(entryRating.getComment());
        EntryStats entryStats = statsService.addOrUpdateStats(entryRatingDto.getTargetId(),
                entryRatingDto.getDataSource(), entryRatingDto.getScore());
        rateResponse.setTotalEntries(entryStats.getTotalEntries());
        rateResponse.setAverageScore(entryStats.getAverageScore());
        return rateResponse;
    }

    @Transactional
    @Override
    public RateResponse rateWithoutComment(EntryRatingWithoutCommentModel entryRatingDto) {
        log.info("Rating without comment using model {}", entryRatingDto);
        entryRatingDto = validateEntryRatingDto(entryRatingDto);
        RateResponse rateResponse = new RateResponse();
        rateResponse.setTargetId(entryRatingDto.getTargetId());
        EntryRating entryRating = entryRatingRepository.save(entryRatingMapper.mapToEntryRating(entryRatingDto));
        rateResponse.setScore(entryRating.getScore());
        EntryStats entryStats = statsService.addOrUpdateStats(entryRatingDto.getTargetId(),
                entryRatingDto.getDataSource(), entryRatingDto.getScore());
        rateResponse.setTotalEntries(entryStats.getTotalEntries());
        rateResponse.setAverageScore(entryStats.getAverageScore());
        return rateResponse;
    }

    @Override
    public StatsResponse findRatingsByTargetIdsAndDataSource(String targetIds, String dataSource) {
        List<RateResponse> stats = new ArrayList<>();
        try {
            String targetIdsDecoded = decodeValue(targetIds);
            log.info("targetIdsDecoded: {}", targetIdsDecoded);
            List<String> targetIdList = Arrays.asList(targetIdsDecoded.split("\\s*,\\s*"));
            for (String targetId : targetIdList) {
                RateResponse rateResponse = new RateResponse();
                EntryStats entryStats = statsService.findStatsByTargetIdAndDataSource(targetId, dataSource);
                rateResponse.setTargetId(targetId);
                rateResponse.setTotalEntries(entryStats.getTotalEntries());
                rateResponse.setAverageScore(entryStats.getAverageScore());
                rateResponse.setScore(0.0);
                rateResponse.setComment("");
                stats.add(rateResponse);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return new StatsResponse(stats);
    }

    @Override
    public List<CommentResponse> findCommentsByTargetIdAndDataSource(String targetId, String dataSource) {
        return entryRatingRepository.findAllByTargetIdAndDataSource(targetId, dataSource).stream()
                .filter(entryRating -> entryRating.getComment() != null && !entryRating.getComment().isEmpty())
                .map(entryRatingMapper::mapToCommentResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAllRatings() {
        log.info("Deleting all ratings");
        entryRatingRepository.deleteAll();
    }

    private String decodeValue(String value) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
    }

    private EntryRatingWithCommentModel validateEntryRatingDto(EntryRatingWithCommentModel entryRatingDto) {
        Double score = entryRatingDto.getScore();
        if (score < MIN_SCORE) {
            score = MIN_SCORE;
        } else if (score > MAX_SCORE) {
            score = MAX_SCORE;
        }
        entryRatingDto.setScore(score);

        String comment = entryRatingDto.getComment();
        if (comment.length() > MAX_COMMENT) {
            comment = comment.substring(0, MAX_COMMENT);
        }
        entryRatingDto.setComment(comment);

        return entryRatingDto;
    }

    private EntryRatingWithoutCommentModel validateEntryRatingDto(EntryRatingWithoutCommentModel entryRatingDto) {
        Double score = entryRatingDto.getScore();
        if (score < MIN_SCORE) {
            score = MIN_SCORE;
        } else if (score > MAX_SCORE) {
            score = MAX_SCORE;
        }
        entryRatingDto.setScore(score);
        return entryRatingDto;
    }

}
