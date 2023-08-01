package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch;

import com.eversis.esa.geoss.curatedrelationships.search.model.BoundingBoxRelation;
import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.PageRequest;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryType;
import com.eversis.esa.geoss.curatedrelationships.search.model.exception.ResourceNotFoundException;
import com.eversis.esa.geoss.curatedrelationships.search.service.search.SearchService;
import com.eversis.esa.geoss.curatedrelationships.search.utils.CollectionMapper;
import com.eversis.esa.geoss.curatedrelationships.search.web.model.ApiError;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper.OpenSearchResponseMapper;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.query.BoundingBoxMapper;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.query.EntryTypeMapper;

import com.rometools.rome.feed.atom.Feed;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * The type Open search controller.
 */
@Tag(name = "OPENSEARCH")
@Slf4j
@RestController
@RequestMapping("/opensearch")
public class OpenSearchController {

    private SearchService searchService;
    private OpenSearchResponseMapper responseMapper;

    /**
     * Instantiates a new Open search controller.
     *
     * @param searchService the search service
     * @param responseMapper the response mapper
     */
    @Autowired
    public OpenSearchController(SearchService searchService, OpenSearchResponseMapper responseMapper) {
        this.searchService = searchService;
        this.responseMapper = responseMapper;
    }

    /**
     * Find by id feed.
     *
     * @param startIndex the start index
     * @param size the size
     * @param targetIds the target ids
     * @param dataSource the data source
     * @return the feed
     */
    @Hidden
    @Operation(summary = "Find resource by their id using opensearch server")
    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_ATOM_XML_VALUE, params = "targetIds")
    public Feed findById(
            @Parameter(description = "Pagination parameter, defines startIndex.")
            @RequestParam(name = "si", defaultValue = "1", required = false) @Min(1) Integer startIndex,
            @Parameter(description = "Pagination parameter, defines items per page.")
            @RequestParam(name = "ct", defaultValue = "10", required = false) @Min(1) Integer size,
            @Parameter(description = "List of entry's ids.")
            @RequestParam(name = "targetIds", required = true) String targetIds,
            @Parameter(description = "Name of the datasource.",
                       schema = @Schema(implementation = DataSource.class))
            @RequestParam(name = "ds") String dataSource) {

        Set<String> ids = CollectionMapper.mapSet(targetIds);
        Page<Entry> resourceEntries = searchService.findResourcesById(
                ids,
                DataSource.fromString(dataSource),
                new PageRequest(--startIndex, size != null ? size : ids.size()));
        return responseMapper.createSearchResultsFeed(resourceEntries);
    }

    /**
     * Find resources feed.
     *
     * @param startIndex the start index
     * @param size the size
     * @param phrase the phrase
     * @param resourceEntryTypes the resource entry types
     * @param parents the parents
     * @param dataSource the data source
     * @param bbox the bbox
     * @param bboxRelation the bbox relation
     * @param startDateTime the start date time
     * @param endDateTime the end date time
     * @param sources the sources
     * @param protocol the protocol
     * @param organisationName the organisation name
     * @param keyword the keyword
     * @param format the format
     * @return the feed
     */
    @Operation(summary = "Search for resources using opensearch server")
    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_ATOM_XML_VALUE)
    public Feed findResources(
            @Parameter(description = "Pagination parameter, defines startIndex.")
            @RequestParam(name = "si", defaultValue = "1") @Min(1) int startIndex,
            @Parameter(description = "Pagination parameter, defines items per page.")
            @RequestParam(name = "ct", defaultValue = "10") @Min(1) int size,
            @Parameter(description = "Phrase. Use to full text search on content.")
            @RequestParam(name = "st", required = false) String phrase,
            @Parameter(description = "Filter by geoss_cr_type",
                       schema = @Schema(implementation = EntryType.class))
            @RequestParam(name = "geoss_cr_type", required = false) String resourceEntryTypes,
            @Parameter(description = "Specify ids of parent entries. Use to get descendants of specified resources.")
            @RequestParam(name = "parents", required = false) String parents,
            @Parameter(description = "Name of the datasource.",
                       schema = @Schema(implementation = DataSource.class))
            @RequestParam(name = "ds") String dataSource,
            @Parameter(
                    description = "Filter by geo coordinates. <br/>Format: leftTopLatitude,leftTopLongitude,"
                                  + "rightBottomLatitude,rightBottomLongitude")
            @RequestParam(name = "bbox", required = false) String bbox,
            @Parameter(description = "Defines bbox relation.",
                       schema = @Schema(implementation = BoundingBoxRelation.class))
            @RequestParam(name = "rel", required = false) String bboxRelation,
            @Parameter(description = "Filter by startDateTime. <br/>Format: yyyy-MM-dd'T'HH:mm:ss'Z'")
            @RequestParam(name = "ts", required = false) @DateTimeFormat(
                    pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") LocalDateTime startDateTime,
            @Parameter(description = "Filter by endDateTime. <br/>Format: yyyy-MM-dd'T'HH:mm:ss'Z'")
            @RequestParam(name = "te", required = false) @DateTimeFormat(
                    pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") LocalDateTime endDateTime,
            @Parameter(description = "Filter by source of the resource.")
            @RequestParam(name = "sources", required = false) String sources,
            @Parameter(description = "Filter by protocol of the resource.")
            @RequestParam(name = "prot", required = false) String protocol,
            @Parameter(description = "Filter by protocol of the resource.")
            @RequestParam(name = "organisationName", required = false) String organisationName,
            @Parameter(description = "Filter by keywords.")
            @RequestParam(name = "kwd", required = false) String keyword,
            @Parameter(description = "Filter by format.")
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

    /**
     * Handle not found error response entity.
     *
     * @param e the e
     * @return the response entity
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundError(ResourceNotFoundException e) {
        log.warn("Failed to find requested resources: " + e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError(LocalDateTime.now(), e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    /**
     * Handle error response entity.
     *
     * @param e the e
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleError(Exception e) {
        log.error("Error occurred during opensearch", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(LocalDateTime.now(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
