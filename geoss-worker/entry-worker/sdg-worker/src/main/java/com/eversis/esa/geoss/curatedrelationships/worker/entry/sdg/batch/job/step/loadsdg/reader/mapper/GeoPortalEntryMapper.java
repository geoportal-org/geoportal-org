package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Organisation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Source;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Type;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config.sdg.SdgProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config.sdg.UNSdgProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.geoportal.GeossSDGNode;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.util.HtmlUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Geo portal entry mapper.
 */
@Component
public class GeoPortalEntryMapper extends BaseSdgMapper {

    private final SdgProperties sdgProperties;
    private final SdgScoreWeightGenerator sdgScoreWeightGenerator;

    /**
     * Instantiates a new Geo portal entry mapper.
     *
     * @param sdgProperties the sdg configuration
     * @param unSdgProperties the un sdg configuration
     * @param sdgScoreWeightGenerator the sdg score weight generator
     */
    @Autowired
    public GeoPortalEntryMapper(
            SdgProperties sdgProperties,
            UNSdgProperties unSdgProperties,
            SdgScoreWeightGenerator sdgScoreWeightGenerator) {
        super(new Organisation(unSdgProperties.getOrganisationTitle()),
                new Source(unSdgProperties.getSourceTitle(), unSdgProperties.getSourceCode()),
                sdgProperties.getEntryCodePrefix());
        this.sdgProperties = sdgProperties;
        this.sdgScoreWeightGenerator = sdgScoreWeightGenerator;
    }

    /**
     * Create geoss sdg indicator entry entry.
     *
     * @param indicator the indicator
     * @return the entry
     */
    public Entry createGeossSdgIndicatorEntry(GeossSDGNode indicator) {
        Entry entry = createSdgEntry(
                indicator.getNumber(),
                "SDG " + indicator.getNumber() + " Indicator",
                HtmlUtil.clearHtml(indicator.getDescription()),
                Type.INFORMATION
        );
        entry.setScoreWeight((double) sdgScoreWeightGenerator.getIndicatorScoreWeight(indicator.getNumber()));
        entry.setLogo(sdgProperties.getDefaultLogo());
        entry.setKeywords(Stream.of("SDG", "SDG Indicators").collect(Collectors.toSet()));
        entry.setTags(Stream.of("indicators", "sdg indicators", "sdg observations", "observations",
                "sustainable development goal observations").collect(Collectors.toSet()));
        return entry;
    }

}
