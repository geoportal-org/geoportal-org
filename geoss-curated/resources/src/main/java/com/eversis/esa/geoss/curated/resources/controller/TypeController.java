package com.eversis.esa.geoss.curated.resources.controller;

import java.util.List;

import com.eversis.esa.geoss.curated.resources.domain.Type;
import com.eversis.esa.geoss.curated.resources.repository.TypeRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Type controller.
 */
@Log4j2
@BasePathAwareController("/type")
@ResponseBody
@Tag(name = "type")
public class TypeController {

    private final TypeRepository typeRepository;

    /**
     * Instantiates a new Type controller.
     *
     * @param typeRepository the type repository
     */
    public TypeController(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    /**
     * Gets type values.
     *
     * @return the type values
     */
    @PreAuthorize("hasAnyRole('RESOURCE_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Type> getTypeValues() {
        log.info("Getting type values...");
        return typeRepository.findByIsCustomOrderByName(0);
    }

}
