package com.eversis.esa.geoss.curated.application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The type Application tests.
 */
@SpringBootTest
@Testcontainers
class ApplicationTests {

    /**
     * The constant MARIA_DB_CONTAINER.
     */
    @Container
    @ServiceConnection
    static MariaDBContainer MARIA_DB_CONTAINER = new MariaDBContainer();

    /**
     * Context loads.
     */
    @Test
    void contextLoads() {
    }
}
