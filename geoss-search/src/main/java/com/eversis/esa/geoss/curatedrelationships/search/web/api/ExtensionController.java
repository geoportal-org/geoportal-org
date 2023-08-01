package com.eversis.esa.geoss.curatedrelationships.search.web.api;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.service.extension.ExtensionSearchService;
import com.eversis.esa.geoss.curatedrelationships.search.utils.CollectionMapper;
import com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.extension.DetailedExtensionDto;
import com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.extension.ExtensionDto;
import com.eversis.esa.geoss.curatedrelationships.search.web.api.mapper.DetailedExtensionDtoMapper;
import com.eversis.esa.geoss.curatedrelationships.search.web.api.mapper.ExtensionDtoMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Extension controller.
 */
@Log4j2
@Tag(name = "EXTENSIONS")
@RequestMapping("/api/extensions")
@RestController
class ExtensionController {

    private final ExtensionSearchService extensionSearchService;
    private final ExtensionDtoMapper extensionMapper;
    private final DetailedExtensionDtoMapper detailedExtensionMapper;

    /**
     * Instantiates a new Extension controller.
     *
     * @param extensionSearchService the extension search service
     * @param extensionMapper the extension mapper
     * @param detailedExtensionMapper the detailed extension mapper
     */
    public ExtensionController(ExtensionSearchService extensionSearchService,
            ExtensionDtoMapper extensionMapper,
            DetailedExtensionDtoMapper detailedExtensionMapper) {
        this.extensionSearchService = extensionSearchService;
        this.extensionMapper = extensionMapper;
        this.detailedExtensionMapper = detailedExtensionMapper;
    }

    /**
     * Gets extensions.
     *
     * @param ids the ids
     * @param dataSourceParam the data source param
     * @return the extensions
     */
    @Operation(summary = "Search for extensions related to specified resources")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ExtensionDto> getExtensions(
            @Parameter(description = "Collection of entry's ids.")
            @RequestParam(name = "ids") String ids,
            @Parameter(description = "Name of the datasource.",
                       schema = @Schema(implementation = DataSource.class))
            @RequestParam(name = "ds") String dataSourceParam) {
        DataSource dataSource = DataSource.fromString(dataSourceParam);
        Set<String> idsCollection = CollectionMapper.mapSet(ids);
        return extensionSearchService.findExtensions(idsCollection, dataSource)
                .stream()
                .map(extensionMapper::mapExtension)
                .collect(Collectors.toList());
    }

    /**
     * Gets detailed extensions.
     *
     * @param ids the ids
     * @param dataSourceParam the data source param
     * @return the detailed extensions
     */
    @Operation(summary = "Search for detailed extensions related to specified resources")
    @GetMapping(path = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DetailedExtensionDto> getDetailedExtensions(
            @Parameter(description = "Collection of entry's ids.")
            @RequestParam(name = "ids") String ids,
            @Parameter(description = "Name of the datasource.",
                       schema = @Schema(implementation = DataSource.class))
            @RequestParam(name = "ds") String dataSourceParam) {
        DataSource dataSource = DataSource.fromString(dataSourceParam);
        Set<String> idsCollection = CollectionMapper.mapSet(ids);
        return extensionSearchService.findExtensions(idsCollection, dataSource)
                .stream()
                .map(detailedExtensionMapper::mapExtension)
                .collect(Collectors.toList());
    }

}
