package com.eversis.esa.geoss.curated.elasticsearch.mapper;

import com.eversis.esa.geoss.curated.elasticsearch.model.EntryExtensionELK;
import com.eversis.esa.geoss.curated.elasticsearch.model.ExtensionELK;
import com.eversis.esa.geoss.curated.elasticsearch.model.RelationExtensionELK;
import com.eversis.esa.geoss.curated.elasticsearch.model.TransferOptionExtensionELK;
import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.domain.TransferOptionExtension;
import com.eversis.esa.geoss.curated.extensions.repository.EntryExtensionRepository;
import com.eversis.esa.geoss.curated.extensions.repository.TransferOptionExtensionRepository;
import com.eversis.esa.geoss.curated.relations.domain.EntryRelation;
import com.eversis.esa.geoss.curated.relations.repository.EntryRelationRepository;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Extension mapper.
 */
@Log4j2
@Component
public class ExtensionMapper {

    private final TransferOptionExtensionRepository transferOptionExtensionRepository;

    private final EntryExtensionRepository entryExtensionRepository;

    private final EntryRelationRepository entryRelationRepository;

    /**
     * Instantiates a new Extension mapper.
     *
     * @param transferOptionExtensionRepository the transfer option extension repository
     * @param entryExtensionRepository the entry extension repository
     * @param entryRelationRepository the entry relation repository
     */
    public ExtensionMapper(TransferOptionExtensionRepository transferOptionExtensionRepository,
            EntryExtensionRepository entryExtensionRepository, EntryRelationRepository entryRelationRepository) {
        this.transferOptionExtensionRepository = transferOptionExtensionRepository;
        this.entryExtensionRepository = entryExtensionRepository;
        this.entryRelationRepository = entryRelationRepository;
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
        entryExtensionELK.setId(extension.getCode());
        entryExtensionELK.setDataSource(extension.getDataSource().getCode());
        entryExtensionELK.setEntryCode(extension.getCode());
        Set<TransferOptionExtension> transferOptions = transferOptionExtensionRepository
                .findByEntryExtension(extension);
        if (transferOptions != null && !transferOptions.isEmpty()) {
            List<TransferOptionExtensionELK> transferOptionELKList = new ArrayList<>();
            for (TransferOptionExtension transferOption : transferOptions) {
                TransferOptionExtensionELK transferOptionELK = mapTransferOption(transferOption);
                transferOptionELKList.add(transferOptionELK);
            }
            entryExtensionELK.setTransferOptions(transferOptionELKList);
        }
        List<EntryExtension> extensions = entryExtensionRepository.findByCode(extension.getCode());
        if (extensions != null && !extensions.isEmpty()) {
            List<ExtensionELK> extensionELKList = new ArrayList<>();
            for (EntryExtension entryExtension : extensions) {
                ExtensionELK extensionELK = mapExtension(entryExtension);
                extensionELKList.add(extensionELK);
            }
            entryExtensionELK.setExtensions(extensionELKList);
        }
        List<EntryRelation> relations = entryRelationRepository.findById_SrcId(extension.getCode());
        if (relations != null && !relations.isEmpty()) {
            List<RelationExtensionELK> relationExtensionELKList = new ArrayList<>();
            for (EntryRelation entryRelation : relations) {
                RelationExtensionELK relationExtensionELK = mapRelation(entryRelation);
                relationExtensionELKList.add(relationExtensionELK);
            }
            entryExtensionELK.setRelations(relationExtensionELKList);
        }
        return entryExtensionELK;
    }

    private RelationExtensionELK mapRelation(EntryRelation entryRelation) {
        RelationExtensionELK relationExtensionELK = new RelationExtensionELK();
        relationExtensionELK.setSrcEntryCode(entryRelation.getId().getSrcId());
        relationExtensionELK.setSrcDataSource(entryRelation.getSrcDataSource().getCode());
        relationExtensionELK.setSrcEntryType(entryRelation.getSrcType().getCode());
        relationExtensionELK.setDestEntryCode(entryRelation.getId().getDestId());
        relationExtensionELK.setDestDataSource(entryRelation.getDestDataSource().getCode());
        relationExtensionELK.setDestEntryType(entryRelation.getDestType().getCode());
        relationExtensionELK.setRelationType(entryRelation.getRelationType().getCode());
        relationExtensionELK.setModifiedDate(entryRelation.getModifiedDate());
        relationExtensionELK.setCreatedDate(entryRelation.getCreateDate());
        return relationExtensionELK;
    }

    private ExtensionELK mapExtension(EntryExtension entryExtension) {
        ExtensionELK extensionELK = new ExtensionELK();
        extensionELK.setEntryExtensionId(entryExtension.getId());
        extensionELK.setSummary(entryExtension.getSummary());
        extensionELK.setUsername(entryExtension.getUsername());
        extensionELK.setUserId(entryExtension.getUserId());
        extensionELK.setKeywords(Stream.of(entryExtension.getKeywords().split(",", -1))
                .collect(Collectors.toList()));
        extensionELK.setModifiedDate(entryExtension.getModifiedDate());
        extensionELK.setCreatedDate(entryExtension.getCreateDate());
        return extensionELK;
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
