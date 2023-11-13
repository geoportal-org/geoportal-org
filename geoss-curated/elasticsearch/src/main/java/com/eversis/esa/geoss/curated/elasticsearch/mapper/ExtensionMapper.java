package com.eversis.esa.geoss.curated.elasticsearch.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.eversis.esa.geoss.curated.elasticsearch.model.EntryExtensionELK;
import com.eversis.esa.geoss.curated.elasticsearch.model.TransferOptionExtensionELK;
import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.domain.TransferOptionExtension;
import com.eversis.esa.geoss.curated.extensions.repository.TransferOptionExtensionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * The type Extension mapper.
 */
@Log4j2
@Component
public class ExtensionMapper {

    private final TransferOptionExtensionRepository transferOptionExtensionRepository;

    /**
     * Instantiates a new Extension mapper.
     *
     * @param transferOptionExtensionRepository the transfer option extension repository
     */
    public ExtensionMapper(TransferOptionExtensionRepository transferOptionExtensionRepository) {
        this.transferOptionExtensionRepository = transferOptionExtensionRepository;
    }

    /**
     * Map to document entry extension elk.
     *
     * @param extension the extension
     * @return the entry extension elk
     */
    public EntryExtensionELK mapToDocument(EntryExtension extension) {
        return getEntryExtensionELK(extension);
    }

    private EntryExtensionELK getEntryExtensionELK(EntryExtension extension) {
        if (extension == null) {
            return null;
        }
        EntryExtensionELK entryExtensionELK = new EntryExtensionELK();
        entryExtensionELK.setId(String.valueOf(extension.getId()));
        entryExtensionELK.setDataSource(extension.getDataSource().getCode());
        entryExtensionELK.setEntryCode(extension.getCode());
        Set<TransferOptionExtension> transferOptions = transferOptionExtensionRepository.findByEntryExtension(extension);
        if (transferOptions != null && !transferOptions.isEmpty()) {
            List<TransferOptionExtensionELK> transferOptionELKList = new ArrayList<>();
            for (TransferOptionExtension transferOption : transferOptions) {
                TransferOptionExtensionELK transferOptionELK = mapTransferOption(transferOption);
                transferOptionELKList.add(transferOptionELK);
            }
            entryExtensionELK.setTransferOptions(transferOptionELKList);
        }
        return entryExtensionELK;
    }

    private TransferOptionExtensionELK mapTransferOption(TransferOptionExtension transferOption) {
        TransferOptionExtensionELK transferOptionELK = new TransferOptionExtensionELK();
        transferOptionELK.setId(String.valueOf(transferOption.getId()));
        transferOptionELK.setName(transferOption.getName());
        transferOptionELK.setDescription(transferOption.getDescription());
        transferOptionELK.setTitle(transferOption.getDisplayTitle());
        transferOptionELK.setProtocol(transferOption.getProtocol().getValue());
        transferOptionELK.setUrl(transferOption.getEndpoint().getUrl());
        transferOptionELK.setUrlType(transferOption.getEndpoint().getUrlType());
        transferOptionELK.setEntryExtensionId(String.valueOf(transferOption.getEntryExtension().getId()));
        return transferOptionELK;
    }

}
