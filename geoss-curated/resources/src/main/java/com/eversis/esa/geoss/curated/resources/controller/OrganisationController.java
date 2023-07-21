package com.eversis.esa.geoss.curated.resources.controller;

import java.util.List;

import com.eversis.esa.geoss.curated.resources.domain.Organisation;
import com.eversis.esa.geoss.curated.resources.repository.OrganisationRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Organisation controller.
 */
@Log4j2
@BasePathAwareController("/organisation")
@ResponseBody
@Tag(name = "organisation")
public class OrganisationController {

    private final OrganisationRepository organisationRepository;

    /**
     * Instantiates a new Organisation controller.
     *
     * @param organisationRepository the organisation repository
     */
    public OrganisationController(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    /**
     * Gets organisation values.
     *
     * @return the organisation values
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Organisation> getOrganisationValues() {
        log.info("Getting organisation values...");
        return organisationRepository.findByIsCustomOrderByTitle(0);
    }

}
