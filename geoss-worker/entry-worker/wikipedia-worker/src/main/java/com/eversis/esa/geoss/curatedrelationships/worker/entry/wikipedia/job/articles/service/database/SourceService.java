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
 * The type Source service.
 */
@Log4j2
@Service
class SourceService {

    @Value("${geoss.curated.wikipedia.source.term}")
    private String wikipediaSourceTerm;
    @Value("${geoss.curated.wikipedia.source.code}")
    private String wikipediaSourceCode;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SELECT_WIKIPEDIA_SOURCE_ID_QUERY = "SELECT id FROM source WHERE code=:code";
    private static final String INSERT_WIKIPEDIA_SOURCE_QUERY = "INSERT "
            + "INTO source(term, code) "
            + "VALUES (?, ?) "
            + "ON DUPLICATE KEY UPDATE term=?, code=?";

    /**
     * Instantiates a new Source service.
     *
     * @param jdbcTemplate the jdbc template
     */
    @Autowired
    public SourceService(@Qualifier("geossCuratedNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = jdbcTemplate;
    }

    /**
     * Returns value of sourceId column, which should be used as foreign key in main entry table for Wikipedia article's
     * data.
     *
     * @return the wikipedia source id
     */
    public Integer getWikipediaSourceId() {
        Integer existingSourceId = getExistingSourceId();
        return existingSourceId != null ? existingSourceId : insertSource();
    }

    private Integer getExistingSourceId() {
        try {
            return namedParameterJdbcTemplate.queryForObject(
                    SELECT_WIKIPEDIA_SOURCE_ID_QUERY,
                    new MapSqlParameterSource().addValue("code", wikipediaSourceCode), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.debug("No source exist in database for Wikipedia");
            return null;
        }
    }

    private Integer insertSource() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.getJdbcTemplate().update(
                connection -> {
                    PreparedStatement ps = connection
                            .prepareStatement(INSERT_WIKIPEDIA_SOURCE_QUERY, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, wikipediaSourceTerm);
                    ps.setString(2, wikipediaSourceCode);
                    ps.setString(3, wikipediaSourceTerm);
                    ps.setString(4, wikipediaSourceCode);
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().intValue();
    }

}
