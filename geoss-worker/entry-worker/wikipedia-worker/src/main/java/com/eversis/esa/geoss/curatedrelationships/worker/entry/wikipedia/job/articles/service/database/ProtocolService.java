package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.service.database;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * The type Protocol service.
 */
@Log4j2
@Service
class ProtocolService {

    @Value("${geoss.curated.wikipedia.protocol.value}")
    private String wikipediaProtocol;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SELECT_WIKIPEDIA_PROTOCOL_ID_QUERY = "SELECT id FROM protocol WHERE value=:value";
    private static final String INSERT_WIKIPEDIA_DATASOURCE_QUERY = "INSERT "
            + "INTO protocol(value, isCustom) "
            + "VALUES (?, 0) "
            + "ON DUPLICATE KEY UPDATE value=?";

    /**
     * Instantiates a new Protocol service.
     *
     * @param jdbcTemplate the jdbc template
     */
    @Autowired
    public ProtocolService(
            @Qualifier("geossCuratedNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = jdbcTemplate;
    }

    /**
     * Returns value of protocolId column, which should be used as foreign key in transfer options table for Wikipedia
     * article's data.
     *
     * @return the wikipedia protocol id
     */
    public Integer getWikipediaProtocolId() {
        Integer existingProtocolId = getExistingProtocolId();
        return existingProtocolId != null ? existingProtocolId : insertProtocol();
    }

    private Integer getExistingProtocolId() {
        try {
            return namedParameterJdbcTemplate.queryForObject(
                    SELECT_WIKIPEDIA_PROTOCOL_ID_QUERY,
                    new MapSqlParameterSource().addValue("value", wikipediaProtocol), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.debug("No protocol exists in database for Wikipedia");
            return null;
        }
    }

    private Integer insertProtocol() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.getJdbcTemplate().update(
                connection -> {
                    PreparedStatement ps = connection
                            .prepareStatement(INSERT_WIKIPEDIA_DATASOURCE_QUERY, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, wikipediaProtocol);
                    ps.setString(2, wikipediaProtocol);
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().intValue();
    }

}
