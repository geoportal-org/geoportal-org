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
 * The type Type service.
 */
@Log4j2
@Service
class TypeService {

    @Value("${geoss.curated.wikipedia.type.code}")
    private String wikiArticleTypeCode;
    @Value("${geoss.curated.wikipedia.type.name}")
    private String wikiArticleTypeName;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SELECT_ARTICLE_TYPE_ID_QUERY = "SELECT id FROM type WHERE code=:code";
    private static final String INSERT_WIKIPEDIA_TYPE_QUERY = "INSERT "
            + "INTO type(name, code) "
            + "VALUES (?, ?) "
            + "ON DUPLICATE KEY UPDATE name=?, code=?";

    /**
     * Instantiates a new Type service.
     *
     * @param jdbcTemplate the jdbc template
     */
    @Autowired
    public TypeService(@Qualifier("geossCuratedNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = jdbcTemplate;
    }

    /**
     * Returns value of typeId column, which should be used as foreign key in main entry table for Wikipedia article's
     * data.
     *
     * @return the artice type id
     */
    public Integer getArticeTypeId() {
        Integer existingTypeId = getExistingTypeId();
        return existingTypeId != null ? existingTypeId : insertType();
    }

    private Integer getExistingTypeId() {
        try {
            return namedParameterJdbcTemplate.queryForObject(
                    SELECT_ARTICLE_TYPE_ID_QUERY,
                    new MapSqlParameterSource().addValue("code", wikiArticleTypeCode), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.debug("No type exist in database for Wikipedia");
            return null;
        }
    }

    private Integer insertType() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.getJdbcTemplate().update(
                connection -> {
                    PreparedStatement ps = connection
                            .prepareStatement(INSERT_WIKIPEDIA_TYPE_QUERY, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, wikiArticleTypeName);
                    ps.setString(2, wikiArticleTypeCode);
                    ps.setString(3, wikiArticleTypeName);
                    ps.setString(4, wikiArticleTypeCode);
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().intValue();
    }

}
