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
 * The type Data source service.
 */
@Log4j2
@Service
class DataSourceService {

    @Value("${geoss.curated.wikipedia.datasource.name}")
    private String wikipediaDataSourceName;
    @Value("${geoss.curated.wikipedia.datasource.code}")
    private String wikipediaDataSourceCode;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SELECT_WIKIPEDIA_DATASOURCE_ID_QUERY = "SELECT id FROM datasource WHERE code=:code";
    private static final String INSERT_WIKIPEDIA_DATASOURCE_QUERY = "INSERT "
            + "INTO datasource(name, code) "
            + "VALUES (?, ?) "
            + "ON DUPLICATE KEY UPDATE name=?, code=?";

    /**
     * Instantiates a new Data source service.
     *
     * @param jdbcTemplate the jdbc template
     */
    @Autowired
    public DataSourceService(
            @Qualifier("geossCuratedNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = jdbcTemplate;
    }

    /**
     * Returns value of sourceId column, which should be used as foreign key in main entry table for Wikipedia article's
     * data.
     *
     * @return the wikipedia data source id
     */
    public Integer getWikipediaDataSourceId() {
        Integer existingSourceId = getExistingDataSourceId();
        return existingSourceId != null ? existingSourceId : insertDataSource();
    }

    private Integer getExistingDataSourceId() {
        try {
            return namedParameterJdbcTemplate.queryForObject(
                    SELECT_WIKIPEDIA_DATASOURCE_ID_QUERY,
                    new MapSqlParameterSource().addValue("code", wikipediaDataSourceCode), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.debug("No data source exist in database for Wikipedia");
            return null;
        }
    }

    private Integer insertDataSource() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.getJdbcTemplate().update(
                connection -> {
                    PreparedStatement ps = connection
                            .prepareStatement(INSERT_WIKIPEDIA_DATASOURCE_QUERY, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, wikipediaDataSourceName);
                    ps.setString(2, wikipediaDataSourceCode);
                    ps.setString(3, wikipediaDataSourceName);
                    ps.setString(4, wikipediaDataSourceCode);
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().intValue();
    }

}
