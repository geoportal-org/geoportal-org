package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Organisation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Source;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Type;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config.sdg.GeoPortalProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config.sdg.SdgProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config.sdg.UNSdgProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.un.UNGoal;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.un.UNIndicator;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.un.UNSeries;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.un.UNTarget;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Un entry mapper.
 */
@Component
public class UNEntryMapper extends BaseSdgMapper {

    private final SdgProperties sdgProperties;
    private final GeoPortalProperties geoPortalProperties;
    private final UNTransferOptionMapper transferOptionMapper;
    private final SdgScoreWeightGenerator sdgScoreWeightGenerator;

    /**
     * Instantiates a new Un entry mapper.
     *
     * @param sdgProperties the sdg configuration
     * @param unSdgProperties the un sdg configuration
     * @param geoPortalProperties the geo portal configuration
     * @param transferOptionMapper the transfer option mapper
     * @param sdgScoreWeightGenerator the sdg score weight generator
     */
    @Autowired
    public UNEntryMapper(
            SdgProperties sdgProperties,
            UNSdgProperties unSdgProperties,
            GeoPortalProperties geoPortalProperties,
            UNTransferOptionMapper transferOptionMapper,
            SdgScoreWeightGenerator sdgScoreWeightGenerator) {
        super(new Organisation(unSdgProperties.getOrganisationTitle()),
                new Source(unSdgProperties.getSourceTitle(), unSdgProperties.getSourceCode()),
                sdgProperties.getEntryCodePrefix());
        this.sdgProperties = sdgProperties;
        this.geoPortalProperties = geoPortalProperties;
        this.transferOptionMapper = transferOptionMapper;
        this.sdgScoreWeightGenerator = sdgScoreWeightGenerator;
    }

    /**
     * Create un goal entry entry.
     *
     * @param goal the goal
     * @return the entry
     */
    public Entry createUNGoalEntry(@NonNull UNGoal goal) {
        Entry entry = createSdgEntry(
                goal.getCode(),
                goal.getCode() + " " + goal.getTitle(),
                goal.getDescription(),
                Type.INFORMATION
        );
        entry.setScoreWeight((double) sdgScoreWeightGenerator.getGoalScoreWeight(goal.getCode()));
        entry.setLogo(getFullSdgGoalImagePath(goal.getCode()));
        entry.setKeywords(Stream.of("Sustainable development goals", "SDG Goals").collect(Collectors.toSet()));
        entry.setTags(Stream.of("sdg", "sustainable development goal", "sustainable development goals", "goals")
                .collect(Collectors.toSet()));
        return entry;
    }

    /**
     * Create un target entry entry.
     *
     * @param target the target
     * @return the entry
     */
    public Entry createUNTargetEntry(@NonNull UNTarget target) {
        Entry entry = createSdgEntry(
                target.getCode(),
                "SDG " + target.getCode() + " Target",
                target.getDescription(),
                Type.INFORMATION
        );
        entry.setScoreWeight((double) sdgScoreWeightGenerator.getTargetScoreWeight(target.getCode()));
        entry.setLogo(sdgProperties.getDefaultLogo());
        entry.setKeywords(Stream.of("SDG", "SDG Targets").collect(Collectors.toSet()));
        entry.setTags(Stream.of("targets", "sdg targets").collect(Collectors.toSet()));
        return entry;
    }

    /**
     * Create un indicator entry entry.
     *
     * @param indicator the indicator
     * @return the entry
     */
    public Entry createUNIndicatorEntry(@NonNull UNIndicator indicator) {
        Entry entry = createSdgEntry(
                indicator.getCode(),
                "SDG " + indicator.getCode() + " Indicator",
                indicator.getDescription(),
                Type.DATA
        );
        entry.setScoreWeight((double) sdgScoreWeightGenerator.getIndicatorScoreWeight(indicator.getCode()));
        entry.setLogo(sdgProperties.getDefaultLogo());
        entry.setKeywords(Stream.of("SDG", "SDG Indicators").collect(Collectors.toSet()));
        entry.setTags(Stream.of("indicators", "sdg indicators", "sdg observations", "observations",
                        "sustainable development goal observations")
                .collect(Collectors.toSet()));
        return entry;
    }

    /**
     * Create un series transfer option transfer option.
     *
     * @param series the series
     * @return the transfer option
     */
    public TransferOption createUNSeriesTransferOption(@NonNull UNSeries series) {
        return transferOptionMapper.createUNSeriesTransferOption(series);
    }

    private String getFullSdgGoalImagePath(String goalCode) {
        String sdgNumberTwoDigits = goalCode.length() == 1 ? "0" + goalCode : goalCode;
        return geoPortalProperties.getBaseUrl() + geoPortalProperties.getBaseImagesEndpoint() + "/sdg/svg/"
               + sdgNumberTwoDigits + "-sdg.svg";
    }

}
