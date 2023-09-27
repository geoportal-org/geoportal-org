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
 * The type Access policy service.
 */
@Log4j2
@Service
class AccessPolicyService {

    @Value("${geoss.curated.wikipedia.access-policy.code}")
    private String accessPolicyCode;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SELECT_ARTICLE_ACCESS_POLICY_ID_QUERY = "SELECT id FROM accesspolicy WHERE code=:code";

    /**
     * Instantiates a new Access policy service.
     *
     * @param jdbcTemplate the jdbc template
     */
    @Autowired
    public AccessPolicyService(
            @Qualifier("geossCuratedNamedParameterJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = jdbcTemplate;
    }

    /**
     * Returns value of accessPolicyId column, which should be used as foreign key in main entry table for Wikipedia
     * article's data.
     *
     * @return the article access policy id
     */
    public Integer getArticleAccessPolicyId() {
        Integer existingDefinitionTypeId = getExistingAccessPolicyId();

        if (existingDefinitionTypeId == null) {
            throw new IllegalStateException("Unable to get accessPolicyId for Wikipedia article");
        }
        return existingDefinitionTypeId;
    }

    private Integer getExistingAccessPolicyId() {
        try {
            return namedParameterJdbcTemplate.queryForObject(
                    SELECT_ARTICLE_ACCESS_POLICY_ID_QUERY,
                    new MapSqlParameterSource().addValue("code", accessPolicyCode), Integer.class);
        } catch (EmptyResultDataAccessException e) {
            log.debug("No access policy exist in database for Wikipedia");
            return null;
        }
    }

}
