package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.reader.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Endpoint;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Protocol;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.UrlType;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.DabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.GeoDabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.reader.dto.Storyline;

import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * The type Storyline transfer option factory.
 */
@Component
class StorylineTransferOptionFactory {

    private final DabProperties dabProperties;
    private final GeoDabProperties geoDabProperties;

    /**
     * Instantiates a new Storyline transfer option factory.
     *
     * @param dabProperties the dab configuration
     * @param geoDabProperties the geo dab configuration
     */
    public StorylineTransferOptionFactory(
            DabProperties dabProperties,
            GeoDabProperties geoDabProperties) {
        this.dabProperties = dabProperties;
        this.geoDabProperties = geoDabProperties;
    }

    /**
     * Create transfer option transfer option.
     *
     * @param storyline the storyline
     * @return the transfer option
     */
    TransferOption createTransferOption(@NonNull Storyline storyline) {
        TransferOption transferOption = new TransferOption();
        transferOption.setName(dabProperties.getEntryCodePrefix() + storyline.getId());
        transferOption.setTitle(geoDabProperties.getStorylinesTransferOptionTitle());
        transferOption.setProtocol(Protocol.KML);
        transferOption.setEndpoint(createWebPageEndpoint(storyline.getWebpageUrl()));
        transferOption.setDescription("Lead: " + storyline.getLead());
        return transferOption;
    }

    private Endpoint createWebPageEndpoint(String url) {
        return new Endpoint(url, UrlType.HTTP.getValue());
    }

}
