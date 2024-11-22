package com.eversis.esa.geoss.personaldata.survey.mapper;

import com.eversis.esa.geoss.personaldata.survey.domain.Survey;
import com.eversis.esa.geoss.personaldata.survey.model.SurveyModel;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

/**
 * The type Survey model to survey converter.
 */
@Log4j2
@Component
public class SurveyModelToSurveyConverter implements Converter<SurveyModel, Survey> {

    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS = {
            DateTimeFormatter.ISO_INSTANT,
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.RFC_1123_DATE_TIME,
            DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH),
            DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss z yyyy")
    };

    @Override
    public Survey convert(SurveyModel surveyModel) {
        Survey survey = new Survey();

        survey.setFrom(surveyModel.getFrom());
        survey.setClassification(surveyModel.getClassification());
        survey.setFoundLookingFor(surveyModel.getFoundLookingFor());
        survey.setLookingFor(surveyModel.getLookingFor());
        survey.setSearchCriteria(surveyModel.getSearchCriteria());
        survey.setVisualization(surveyModel.getVisualization());
        survey.setAdequately(surveyModel.getAdequately());
        survey.setInterest(surveyModel.getInterest());
        survey.setOrganized(surveyModel.getOrganized());
        survey.setImpression(surveyModel.getImpression());

        survey.getAuditable().setCreatedDate(parse(surveyModel.getDateTime()));

        return survey;
    }

    private Instant parse(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        for (DateTimeFormatter dateTimeFormatter : DATE_TIME_FORMATTERS) {
            try {
                TemporalAccessor parse = dateTimeFormatter.parse(dateTime);
                return Instant.from(parse);
            } catch (DateTimeException e) {
                log.debug(dateTime + " " + e.getMessage());
            }
        }
        throw new DateTimeParseException("Cannot parse dateTime", dateTime, 0);
    }
}
