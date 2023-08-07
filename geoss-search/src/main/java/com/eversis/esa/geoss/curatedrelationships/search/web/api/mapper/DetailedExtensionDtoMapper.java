package com.eversis.esa.geoss.curatedrelationships.search.web.api.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Extension;
import com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.extension.DetailedEntryExtensionDto;
import com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.extension.DetailedExtensionDto;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Detailed extension dto mapper.
 */
@Component
public class DetailedExtensionDtoMapper {

    /**
     * Map extension detailed extension dto.
     *
     * @param extension the extension
     * @return the detailed extension dto
     */
    public DetailedExtensionDto mapExtension(Extension extension) {
        if (extension == null) {
            return null;
        }

        return new DetailedExtensionDto(
                extension.getCode(),
                extension.getDataSource(),
                mapEntryExtensions(extension),
                new ArrayList<>(extension.getRelations()));
    }

    private List<DetailedEntryExtensionDto> mapEntryExtensions(@NotNull Extension extension) {
        return extension.getEntryExtensions().stream()
                .map(entryExtension -> DetailedEntryExtensionDto.builder()
                        .entryExtensionId(entryExtension.getEntryExtensionId())
                        .createdDate(entryExtension.getCreatedDate())
                        .modifiedDate(entryExtension.getModifiedDate())
                        .userName(entryExtension.getUsername())
                        .userId(entryExtension.getUserId())
                        .summary(entryExtension.getSummary())
                        .keywords(entryExtension.getKeywords())
                        .transferOptions(extension.getTransferOptions()
                                .stream()
                                .filter(transferOption -> transferOption.getEntryExtensionId()
                                        .equals(entryExtension.getEntryExtensionId()))
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

}
