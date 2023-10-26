package com.eversis.esa.geoss.common.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Ping controller.
 */
@Log4j2
@Hidden
@RestController
@RequestMapping("/ping")
public class PingController {

    /**
     * Test string.
     *
     * @return the string
     */
    @GetMapping
    public String test() {
        log.info("ping:pong");
        return "PONG";
    }
}
