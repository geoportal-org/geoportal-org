package com.eversis.esa.geoss.curated.common.controller;

import com.eversis.esa.geoss.curated.common.domain.Protocol;
import com.eversis.esa.geoss.curated.common.repository.ProtocolRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * The type Protocol controller.
 */
@Log4j2
@BasePathAwareController("/protocol")
@ResponseBody
@Tag(name = "protocol")
public class ProtocolController {

    private final ProtocolRepository protocolRepository;

    /**
     * Instantiates a new Protocol controller.
     *
     * @param protocolRepository the protocol repository
     */
    public ProtocolController(ProtocolRepository protocolRepository) {
        this.protocolRepository = protocolRepository;
    }

    /**
     * Gets protocol values.
     *
     * @return the protocol values
     */
    @PreAuthorize("hasAnyRole('RESOURCE_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Protocol> getProtocolValues() {
        log.info("Getting protocol values...");
        return protocolRepository.findByIsCustomOrderByValue(0);
    }

}
