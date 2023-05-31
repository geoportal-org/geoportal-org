package com.eversis.esa.geoss.personaldata.survey.search;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Search query parser.
 */
@Log4j2
public class SearchQueryParser {

    private static final Map<String, String> OPERATORS = Map.of(
            "gt", ">",
            "lt", "<",
            "ge", ">:",
            "le", "<:",
            "ne", "!:",
            "eq", ":",
            "like", "~",
            "and", "&",
            "or", "|"
    );

    private static final Pattern OPERATOR_PATTERN = Pattern.compile(
            "(?: (gt|lt|ge|le|ne|eq|like|and|or) )?('.*?'|\\w+|\\S)");

    private static final Pattern NON_WHITESPACE_PATTERN = Pattern.compile(
            "(?:\\s+|\\v+|\\h+)?('(?<=').*?(?=')'|\\w+|\\S)");

    private static final Pattern QUERY_PATTERN = Pattern.compile(
            "([&|])?(\\w+?)(>|<|>:|<:|!:|:|~)'?((?<=').*?(?=')|\\w+)'?");

    /**
     * Parse list.
     *
     * @param query the query
     * @return the list
     */
    public List<SearchQuery> parse(final String query) {
        log.debug("query:{}", query);

        String search = query.strip();
        search = translateOperators(search);
        log.debug("search:{}", search);

        search = removeWhitespace(search);
        log.debug("search:{}", search);

        List<SearchQuery> searchQueries = new ArrayList<>();
        Matcher matcher = QUERY_PATTERN.matcher(search);
        while (matcher.find()) {
            String logic = matcher.group(1);
            String key = matcher.group(2);
            String operator = matcher.group(3);
            String value = matcher.group(4);
            log.trace("{} {} {} {}", logic, key, operator, value);
            SearchQuery searchQuery = new SearchQuery(logic, key, operator, value);
            searchQueries.add(searchQuery);
        }
        return searchQueries;
    }

    private String translateOperators(String query) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = OPERATOR_PATTERN.matcher(query);
        while (matcher.find()) {
            String group = matcher.group(1);
            if (group != null) {
                sb.append(OPERATORS.get(group));
            }
            sb.append(matcher.group(2));
        }
        return sb.toString();
    }

    private String removeWhitespace(String query) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = NON_WHITESPACE_PATTERN.matcher(query);
        while (matcher.find()) {
            sb.append(matcher.group(1));
        }
        return sb.toString();
    }
}
