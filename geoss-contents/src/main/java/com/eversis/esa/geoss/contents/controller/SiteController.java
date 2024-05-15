package com.eversis.esa.geoss.contents.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The type Site controller.
 */
@Log4j2
@RepositoryRestController("/site")
@ResponseBody
@Tag(name = "sites")
public class SiteController {

}
