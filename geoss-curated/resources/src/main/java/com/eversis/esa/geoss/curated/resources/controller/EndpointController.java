package com.eversis.esa.geoss.curated.resources.controller;

import java.util.List;

import com.eversis.esa.geoss.curated.resources.domain.Endpoint;
import com.eversis.esa.geoss.curated.resources.repository.EndpointRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
        return endpointRepository.findByIsCustomOrderByUrl(0);
    }

}
