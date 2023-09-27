package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.service.database;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * The type Definition type service.
 */
@Log4j2
@Service
class DefinitionTypeService {

    @Value("${geoss.curated.wikipedia.definition-type.code}")
    private String definitionTypeCode;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SELECT_ARTICLE_DEFINITION_TYPE_ID_QUERY = "SELECT id FROM definitiontype WHERE"
                                                                          + " code=:code";

    /**
     * Instantiates a new Definition type service.
     *
     * @param jdbcTemplate the jdbc template
     */
    @Autowired
    public DefinitionTypeService(
            @Qualifier("geossCuratedNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = jdbcTemplate;
    }

    /**
     * Returns value of definitionTypeId column, which should be used as foreign key in main entry table for Wikipedia
     * article's data.
     *
     * @return the article definition type id
     */
    public Integer getArticleDefinitionTypeId() {
        Integer existingDefinitionTypeId = getExistingDefinitionTypeId();

        if (existingDefinitionTypeId == null) {
            throw new IllegalStateException("Unable to get definitionTypeId for Wikipedia article");
        }
        return existingDefinitionTypeId;
    }

    private Integer getExistingDefinitionTypeId() {
        try {
            return namedParameterJdbcTemplate.queryForObject(
                    SELECT_ARTICLE_DEFINITION_TYPE_ID_QUERY,
                    new MapSqlParameterSource().addValue("code", definitionTypeCode), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.debug("No definition type exist in database for Wikipedia");
            return null;
        }
    }

}
