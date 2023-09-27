package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Protocol;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.un.UNSeries;

import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * The type Un transfer option mapper.
 */
@Component
class UNTransferOptionMapper {

    private final UNSeriesEndpointFactory endpointFactory;

    /**
     * Instantiates a new Un transfer option mapper.
     *
     * @param endpointFactory the endpoint factory
     */
    public UNTransferOptionMapper(UNSeriesEndpointFactory endpointFactory) {
        this.endpointFactory = endpointFactory;
    }

    /**
     * Create un series transfer option transfer option.
     *
     * @param series the series
     * @return the transfer option
     */
    public TransferOption createUNSeriesTransferOption(@NonNull UNSeries series) {
        TransferOption transferOption = new TransferOption();
        transferOption.setName(series.getCode());
        transferOption.setTitle(series.getDescription());
        transferOption.setProtocol(Protocol.SDG);
        transferOption.setEndpoint(endpointFactory.createUNSeriesEndpoint(series.getCode()));
        return transferOption;
    }

}
