package com.eversis.esa.geoss.curatedrelationships.search.web.api;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.service.extension.ExtensionSearchService;
import com.eversis.esa.geoss.curatedrelationships.search.utils.CollectionMapper;
import com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.extension.DetailedExtensionDto;
import com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.extension.ExtensionDto;
import com.eversis.esa.geoss.curatedrelationships.search.web.api.mapper.DetailedExtensionDtoMapper;
import com.eversis.esa.geoss.curatedrelationships.search.web.api.mapper.ExtensionDtoMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = {"EXTENSIONS"})
@RequestMapping("/api/extensions")
@RestController
class ExtensionController {

    private final ExtensionSearchService extensionSearchService;
    private final ExtensionDtoMapper extensionMapper;
    private final DetailedExtensionDtoMapper detailedExtensionMapper;

    public ExtensionController(ExtensionSearchService extensionSearchService,
            ExtensionDtoMapper extensionMapper,
            DetailedExtensionDtoMapper detailedExtensionMapper) {
        this.extensionSearchService = extensionSearchService;
        this.extensionMapper = extensionMapper;
        this.detailedExtensionMapper = detailedExtensionMapper;
    }

    @ApiOperation(value = "Search for extensions related to specified resources")
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ExtensionDto> getExtensions(
            @ApiParam(value = "Collection of entry's ids.")
            @RequestParam(name = "ids") String ids,
            @ApiParam(value = "Name of the datasource.", allowableValues = "geoss_cr, amerigeoss_ckan, wikipedia, zenodo")
            @RequestParam(name = "ds") String dataSourceParam) {
        DataSource dataSource = DataSource.fromString(dataSourceParam);
        Set<String> idsCollection = CollectionMapper.mapSet(ids);
        return extensionSearchService.findExtensions(idsCollection, dataSource)
                .stream()
                .map(extensionMapper::mapExtension)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Search for detailed extensions related to specified resources")
    @GetMapping(path = "/details", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<DetailedExtensionDto> getDetailedExtensions(
            @ApiParam(value = "Collection of entry's ids.")
            @RequestParam(name = "ids") String ids,
            @ApiParam(value = "Name of the datasource.", allowableValues = "geoss_cr, amerigeoss_ckan, wikipedia, zenodo")
            @RequestParam(name = "ds") String dataSourceParam) {
        DataSource dataSource = DataSource.fromString(dataSourceParam);
        Set<String> idsCollection = CollectionMapper.mapSet(ids);
        return extensionSearchService.findExtensions(idsCollection, dataSource)
                .stream()
                .map(detailedExtensionMapper::mapExtension)
                .collect(Collectors.toList());
    }

}
