package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch;

import com.eversis.esa.geoss.curatedrelationships.search.model.BoundingBoxRelation;
import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.PageRequest;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.model.exception.ResourceNotFoundException;
import com.eversis.esa.geoss.curatedrelationships.search.service.search.SearchService;
import com.eversis.esa.geoss.curatedrelationships.search.utils.CollectionMapper;
import com.eversis.esa.geoss.curatedrelationships.search.web.model.ApiError;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper.OpenSearchResponseMapper;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.query.BoundingBoxMapper;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.query.EntryTypeMapper;

import com.rometools.rome.feed.atom.Feed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Set;
import javax.validation.constraints.Min;

@Api(tags = {"OPENSEARCH"})
@Slf4j
@RestController
@RequestMapping("/opensearch")
public class OpenSearchController {

    private SearchService searchService;
    private OpenSearchResponseMapper responseMapper;

    @Autowired
    public OpenSearchController(SearchService searchService, OpenSearchResponseMapper responseMapper) {
        this.searchService = searchService;
        this.responseMapper = responseMapper;
    }

    @ApiOperation(value = "Find resource by their id using opensearch server")
    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_ATOM_XML_VALUE, params = "targetIds")
    public Feed findById(
            @ApiParam(value = "Pagination parameter, defines startIndex.", defaultValue = "1")
            @RequestParam(name = "si", defaultValue = "1", required = false) @Min(1) Integer startIndex,
            @ApiParam(value = "Pagination parameter, defines items per page.", defaultValue = "10")
            @RequestParam(name = "ct", defaultValue = "10", required = false) @Min(1) Integer size,
            @ApiParam(value = "List of entry's ids.")
            @RequestParam(name = "targetIds", required = true) String targetIds,
            @ApiParam(value = "Name of the datasource.", allowableValues = "geoss_cr, amerigeoss_ckan")
            @RequestParam(name = "ds") String dataSource) {

        Set<String> ids = CollectionMapper.mapSet(targetIds);
        Page<Entry> resourceEntries = searchService.findResourcesById(
                ids,
                DataSource.fromString(dataSource),
                new PageRequest(--startIndex, size != null ? size : ids.size()));
        return responseMapper.createSearchResultsFeed(resourceEntries);
    }

    @ApiOperation(value = "Search for resources using opensearch server")
    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public Feed findResources(
            @ApiParam(value = "Pagination parameter, defines startIndex.", defaultValue = "1")
            @RequestParam(name = "si", defaultValue = "1") @Min(1) int startIndex,
            @ApiParam(value = "Pagination parameter, defines items per page.", defaultValue = "10")
            @RequestParam(name = "ct", defaultValue = "10") @Min(1) int size,
            @ApiParam(value = "Phrase. Use to full text search on content.")
            @RequestParam(name = "st", required = false) String phrase,
            @ApiParam(value = "Filter by geoss_cr_type", allowableValues = "data_resource, service_resource, information_resource")
            @RequestParam(name = "geoss_cr_type", required = false) String resourceEntryTypes,
            @ApiParam(value = "Specify ids of parent entries. Use to get descendants of specified resources.")
            @RequestParam(name = "parents", required = false) String parents,
            @ApiParam(value = "Name of the datasource.", allowableValues = "geoss_cr, amerigeoss_ckan")
            @RequestParam(name = "ds") String dataSource,
            @ApiParam(value = "Filter by geo coordinates. <br/>Format: leftTopLatitude,leftTopLongitude,rightBottomLatitude,rightBottomLongitude")
            @RequestParam(name = "bbox", required = false) String bbox,
            @ApiParam(value = "Defines bbox relation.", allowableValues = "CONTAINS, OVERLAPS, DISJOINT")
            @RequestParam(name = "rel", required = false) String bboxRelation,
            @ApiParam(value = "Filter by startDateTime. <br/>Format: yyyy-MM-dd'T'HH:mm:ss'Z'")
            @RequestParam(name = "ts", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") LocalDateTime startDateTime,
            @ApiParam(value = "Filter by endDateTime. <br/>Format: yyyy-MM-dd'T'HH:mm:ss'Z'")
            @RequestParam(name = "te", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") LocalDateTime endDateTime,
            @ApiParam(value = "Filter by source of the resource.")
            @RequestParam(name = "sources", required = false) String sources,
            @ApiParam(value = "Filter by protocol of the resource.")
            @RequestParam(name = "prot", required = false) String protocol,
            @ApiParam(value = "Filter by protocol of the resource.")
            @RequestParam(name = "organisationName", required = false) String organisationName,
            @ApiParam(value = "Filter by keywords.")
            @RequestParam(name = "kwd", required = false) String keyword,
            @ApiParam(value = "Filter by format.")
            @RequestParam(name = "frmt", required = false) String format
    ) {
        SearchQuery searchQuery = new SearchQuery.SearchQueryBuilder()
                .setPhrase(phrase)
                .setBoundingBox(BoundingBoxMapper.mapFromString(bbox))
                .setBoundingBoxRelation(BoundingBoxRelation.fromString(bboxRelation))
                .setDateRange(startDateTime, endDateTime)
                .setSources(CollectionMapper.mapSet(sources))
                .setEntryTypes(EntryTypeMapper.mapFromString(resourceEntryTypes))
                .setParents(CollectionMapper.mapSet(parents))
                .setDataSource(DataSource.fromString(dataSource))
                .setProtocol(protocol)
                .setOrganizationName(organisationName)
                .setKeyword(keyword)
                .setFormat(format)
                .build();
        FacetedPage<Entry> resourceEntries = searchService.findResources(
                searchQuery,
                new PageRequest(--startIndex, size));
        return responseMapper.createFacetedSearchResultsFeed(resourceEntries);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundError(ResourceNotFoundException e) {
        log.warn("Failed to find requested resources: " + e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError(LocalDateTime.now(), e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleError(Exception e) {
        log.error("Error occurred during opensearch", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(LocalDateTime.now(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
