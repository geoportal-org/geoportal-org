package com.eversis.esa.geoss.curated.common.controller;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.common.service.DataSourceService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * The type Data source controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/data-sources")
@ResponseBody
@Tag(name = "data-sources")
public class DataSourceController {

    private final DataSourceService dataSourceService;

    /**
     * Data sources codes list.
     *
     * @return the list
     */
    @RequestMapping(path = "/codes", method = RequestMethod.OPTIONS)
    List<String> dataSourcesCodes() {
        return dataSourceService.getDataSourcesCodes();
    }

    /**
     * Data sources list.
     *
     * @return the list
     */
    @GetMapping
    List<DataSource> dataSources() {
        return dataSourceService.getDataSources();
    }
}
