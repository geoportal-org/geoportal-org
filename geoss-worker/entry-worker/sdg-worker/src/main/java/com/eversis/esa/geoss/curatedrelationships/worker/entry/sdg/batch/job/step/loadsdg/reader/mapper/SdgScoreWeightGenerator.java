package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.mapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * The type Sdg score weight generator.
 */
@Log4j2
@Validated
@Component
class SdgScoreWeightGenerator {

    private static final int DEFAULT_VALUE = 1;

    /**
     * Gets goal score weight.
     *
     * @param number the number
     * @return the goal score weight
     */
    public int getGoalScoreWeight(String number) {
        try {
            return 100_000 - Integer.parseInt(number);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse provided number: {}", number);
            return DEFAULT_VALUE;
        }
    }

    /**
     * Gets target score weight.
     *
     * @param number the number
     * @return the target score weight
     */
    public int getTargetScoreWeight(String number) {
        try {
            String[] numbers = splitSdgNumber(number, 2);
            String targetValue = numbers[numbers.length - 1];
            if (isInteger(targetValue)) {
                return 10_000 - Integer.parseInt(targetValue);
            }
            return 9_000 - numbers[numbers.length - 1].charAt(0);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse provided number: {}", number);
            return DEFAULT_VALUE;
        }
    }

    /**
     * Gets indicator score weight.
     *
     * @param number the number
     * @return the indicator score weight
     */
    public int getIndicatorScoreWeight(String number) {
        try {
            String[] numbers = splitSdgNumber(number, 3);
            String indicatorValue = numbers[numbers.length - 1];
            if (isInteger(indicatorValue)) {
                return 1000 - Integer.parseInt(indicatorValue);
            }
            return 900 - numbers[numbers.length - 1].charAt(0);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse provided number: {}", number);
            return DEFAULT_VALUE;
        }
    }

    private static String[] splitSdgNumber(@NotBlank String concatenatedNumber, @Min(1) int expectedValuesCount) {
        String[] numbers = concatenatedNumber.split("\\.");
        if (numbers.length != expectedValuesCount) {
            throw new IllegalArgumentException(
                    "SDG Target number should contain " + expectedValuesCount + " values separated by dot");
        }
        return numbers;
    }

    private static boolean isInteger(String strNum) {
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

}
