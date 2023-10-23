package com.eversis.esa.geoss.curated.common.controller;

import com.eversis.esa.geoss.curated.common.domain.Endpoint;
import com.eversis.esa.geoss.curated.common.repository.EndpointRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * The type Endpoint controller.
 */
@Log4j2
@BasePathAwareController("/endpoint")
@ResponseBody
@Tag(name = "endpoint")
public class EndpointController {

    private final EndpointRepository endpointRepository;

    /**
     * Instantiates a new Endpoint controller.
     *
     * @param endpointRepository the endpoint repository
     */
    public EndpointController(EndpointRepository endpointRepository) {
        this.endpointRepository = endpointRepository;
    }

    /**
     * Gets endpoint values.
     *
     * @return the endpoint values
     */
    @PreAuthorize("hasAnyRole('RESOURCE_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Endpoint> getEndpointValues() {
        log.info("Getting endpoint values...");
        return endpointRepository.findByIsCustomOrderByUrl(0, PageRequest.of(0, 10));
    }

    /**
     * Gets endpoint url type values.
     *
     * @return the endpoint url type values
     */
    @PreAuthorize("hasAnyRole('RESOURCE_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/urlTypes")
    public List<String> getEndpointUrlTypeValues() {
        log.info("Getting endpoint url type values...");
        return endpointRepository.findEndpointUrlTypeValues();
    }

    /**
     * Find endpoints list.
     *
     * @param query the query
     * @return the list
     */
    @PreAuthorize("hasAnyRole('RESOURCE_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find")
    public List<Endpoint> findEndpoints(@RequestParam("query") @NotNull @Size(min = 2) String query) {
        return endpointRepository.findEndpoints(query, PageRequest.of(0, 10));
    }

}
