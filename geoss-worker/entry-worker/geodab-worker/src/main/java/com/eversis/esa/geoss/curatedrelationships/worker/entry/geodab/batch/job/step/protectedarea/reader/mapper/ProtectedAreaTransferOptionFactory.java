package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.reader.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Endpoint;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Protocol;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.UrlType;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.DabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.GeoDabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.reader.dto.ProtectedArea;

import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * The type Protected area transfer option factory.
 */
@Component
class ProtectedAreaTransferOptionFactory {

    private final DabProperties dabProperties;
    private final GeoDabProperties geoDabProperties;

    /**
     * Instantiates a new Protected area transfer option factory.
     *
     * @param dabProperties the dab configuration
     * @param geoDabProperties the geo dab configuration
     */
    public ProtectedAreaTransferOptionFactory(
            DabProperties dabProperties,
            GeoDabProperties geoDabProperties) {
        this.dabProperties = dabProperties;
        this.geoDabProperties = geoDabProperties;
    }

    /**
     * Create transfer option transfer option.
     *
     * @param protectedArea the protected area
     * @return the transfer option
     */
    TransferOption createTransferOption(@NonNull ProtectedArea protectedArea) {
        TransferOption transferOption = new TransferOption();
        transferOption.setName(dabProperties.getEntryCodePrefix() + protectedArea.getId());
        transferOption.setTitle(geoDabProperties.getProtectedAreasTransferOptionTitle());
        transferOption.setProtocol(Protocol.KML);
        transferOption.setEndpoint(createKmlEndpoint(protectedArea.getKmlUrl()));
        transferOption.setDescription("Lat:" + protectedArea.getLat() + " Lon:" + protectedArea.getLon());
        return transferOption;
    }

    private Endpoint createKmlEndpoint(String url) {
        return new Endpoint(url, UrlType.KML.getValue());
    }

}
