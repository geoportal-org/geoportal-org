package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

class SdgScoreWeightGeneratorTests {

    private SdgScoreWeightGenerator sdgScoreWeightGenerator;

    @BeforeEach
    void beforeEach() {
        sdgScoreWeightGenerator = new SdgScoreWeightGenerator();
    }

    @ParameterizedTest
    @MethodSource("sdgGoalProvider")
    void whenSdgGoalsAreCompared_thenScoreWeightShouldMatchTheirOrder(String highestSdgNumber, String lowerSdgNumber,
            String lowestSdgNumber) {
        double highestSdgScoreWeight = sdgScoreWeightGenerator.getGoalScoreWeight(highestSdgNumber);
        double lowerSdgScoreWeight = sdgScoreWeightGenerator.getGoalScoreWeight(lowerSdgNumber);
        double lowestSdgScoreWeight = sdgScoreWeightGenerator.getGoalScoreWeight(lowestSdgNumber);

        assertThat(highestSdgScoreWeight, is(greaterThan(lowerSdgScoreWeight)));
        assertThat(lowerSdgScoreWeight, is(greaterThan(lowestSdgScoreWeight)));
    }

    private static Stream<Arguments> sdgGoalProvider() {
        return Stream.of(
                Arguments.of("1", "2", "3"),
                Arguments.of("1", "10", "17")
        );
    }

    @ParameterizedTest
    @MethodSource("sdgTargetProvider")
    void whenSdgTargetsAreCompared_thenScoreWeightShouldMatchTheirOrder(String highestSdgNumber, String lowerSdgNumber,
            String lowestSdgNumber) {
        double highestSdgScoreWeight = sdgScoreWeightGenerator.getTargetScoreWeight(highestSdgNumber);
        double lowerSdgScoreWeight = sdgScoreWeightGenerator.getTargetScoreWeight(lowerSdgNumber);
        double lowestSdgScoreWeight = sdgScoreWeightGenerator.getTargetScoreWeight(lowestSdgNumber);

        assertThat(highestSdgScoreWeight, is(greaterThan(lowerSdgScoreWeight)));
        assertThat(lowerSdgScoreWeight, is(greaterThan(lowestSdgScoreWeight)));
    }

    private static Stream<Arguments> sdgTargetProvider() {
        return Stream.of(
                Arguments.of("1.1", "1.2", "1.3"),
                Arguments.of("1.1", "1.10", "1.17"),
                Arguments.of("1.1", "1.10", "1.a"),
                Arguments.of("1.a", "1.b", "1.d")
        );
    }

    @ParameterizedTest
    @MethodSource("sdgIndicatorProvider")
    void whenSdgIndicatorsAreCompared_thenScoreWeightShouldMatchTheirOrder(String highestSdgNumber,
            String lowerSdgNumber, String lowestSdgNumber) {
        double highestSdgScoreWeight = sdgScoreWeightGenerator.getIndicatorScoreWeight(highestSdgNumber);
        double lowerSdgScoreWeight = sdgScoreWeightGenerator.getIndicatorScoreWeight(lowerSdgNumber);
        double lowestSdgScoreWeight = sdgScoreWeightGenerator.getIndicatorScoreWeight(lowestSdgNumber);

        assertThat(highestSdgScoreWeight, is(greaterThan(lowerSdgScoreWeight)));
        assertThat(lowerSdgScoreWeight, is(greaterThan(lowestSdgScoreWeight)));
    }

    private static Stream<Arguments> sdgIndicatorProvider() {
        return Stream.of(
                Arguments.of("1.1.1", "1.1.2", "1.1.3"),
                Arguments.of("1.1.1", "1.1.10", "1.1.17"),
                Arguments.of("1.1.1", "1.1.2", "1.1.a"),
                Arguments.of("1.1.1", "1.1.10", "1.1.b"),
                Arguments.of("1.1.a", "1.1.b", "1.1.c")
        );
    }
}
