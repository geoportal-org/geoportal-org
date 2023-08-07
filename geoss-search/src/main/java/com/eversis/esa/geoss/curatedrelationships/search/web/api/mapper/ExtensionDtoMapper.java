package com.eversis.esa.geoss.curatedrelationships.search.web.api.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryExtension;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryRelation;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Extension;
import com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.extension.EntryExtensionDto;
import com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.extension.ExtensionDto;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Extension dto mapper.
 */
@Component
public class ExtensionDtoMapper {

    private final EntryTypeMapper entryTypeMapper;

    /**
     * Instantiates a new Extension dto mapper.
     *
     * @param entryTypeMapper the entry type mapper
     */
    public ExtensionDtoMapper(EntryTypeMapper entryTypeMapper) {
        this.entryTypeMapper = entryTypeMapper;
    }

    /**
     * Map extension extension dto.
     *
     * @param extension the extension
     * @return the extension dto
     */
    public ExtensionDto mapExtension(Extension extension) {
        if (extension == null) {
            return null;
        }

        ExtensionDto extensionDto = new ExtensionDto(extension.getCode(), extension.getDataSource());
        extensionDto.setEntryExtensions(mapEntryExtensions(extension.getEntryExtensions()));
        extensionDto.setTransferOptions(extension.getTransferOptions());
        extensionDto.setRelations(extension.getRelations());
        extensionDto.setDataHubTypes(
                getSubRelationTypes(extension.getCode(), extension.getDataSource(), extension.getRelations()));
        return extensionDto;
    }

    private Set<EntryExtensionDto> mapEntryExtensions(@NotNull Set<EntryExtension> entryExtensions) {
        return entryExtensions.stream()
                .map(entryExtension -> EntryExtensionDto.builder()
                        .entryExtensionId(entryExtension.getEntryExtensionId())
                        .summary(entryExtension.getSummary())
                        .keywords(entryExtension.getKeywords())
                        .build())
                .collect(Collectors.toSet());
    }

    private Set<String> getSubRelationTypes(@NotNull String entryCode, @NotNull DataSource dataSource,
            Set<EntryRelation> relations) {
        if (relations == null) {
            return Collections.emptySet();
        }
        return relations.stream()
                .filter(entryRelation -> entryRelation.getSrcDataSource() == dataSource
                                         && entryRelation.getSrcEntryCode().equals(entryCode))
                .map(EntryRelation::getDestType)
                .map(entryTypeMapper::getHubType)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

}
